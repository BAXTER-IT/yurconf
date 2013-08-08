/**
 * 
 */
package com.baxter.config.processor.repo.file;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Enumeration;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baxter.config.om.ConfigID;
import com.baxter.config.om.Version;
import com.baxter.config.processor.AbstractProcessor;
import com.baxter.config.processor.ConfigurationRepository;
import com.baxter.config.processor.ProcessorException;
import com.baxter.config.processor.ProcessorFactory;
import com.baxter.config.processor.desc.Descriptor;
import com.baxter.config.processor.desc.Loader;
import com.baxter.config.processor.desc.Parameter;
import com.baxter.config.processor.desc.Processor;
import com.baxter.config.processor.desc.Upgrade;
import com.baxter.config.processor.repo.RepositoryException;

/**
 * Processor Factory that returns a target processor for specified input.
 * 
 * @TODO extract the package introspection into the new class
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public final class ProcessorFactoryImpl implements ProcessorFactory
{

  /**
   * Logger instance.
   */
  private static final Logger LOGGER = LoggerFactory
      .getLogger(ProcessorFactoryImpl.class);

  /**
   * Name of the processor descriptor resource.
   */
  private static final String DESCRIPTOR_RESOURCE = "META-INF/services/com.baxter.config.processor.xml";

  /**
   * BeanUtils instance.
   */
  private static final BeanUtilsBean BUB = BeanUtilsBean.getInstance();

  /**
   * Processors cache.
   */
  private final ProcessorsCache processorsCache = new ProcessorsCache();

  /**
   * Configuration repository.
   */
  private final ConfigurationRepository repository;

  /**
   * Hidden constructor.
   * ProcessorFactory
   * @param repositoryRoot
   *          configuration repository root
   * @throws ProcessorException
   *           if cannot create factory
   */
  private ProcessorFactoryImpl(final File repositoryRoot) throws ProcessorException
  {
    LOGGER.trace("Creating factory with repository at {}",
        repositoryRoot.getAbsolutePath());
    this.repository = new RepositoryImpl(repositoryRoot);
    loadProcessors();
  }

  /**
   * Returns the factory instance for specified repository.
   * 
   * @param repository
   *          root of configuration repository
   * @return processor factory instance
   * @throws ProcessorException
   *           if cannot get factory
   */
  public static ProcessorFactory getInstance(final File repository)
      throws ProcessorException
  {
    return new ProcessorFactoryImpl(repository);
  }

  /* (non-Javadoc)
   * @see com.baxter.config.processor.ProcessorFactory#getProcessor(com.baxter.config.om.ConfigID, com.baxter.config.om.Version)
   */
  @Override
  public AbstractProcessor getProcessor(final ConfigID configId,
      final Version version) throws ProcessorException
  {
    final AbstractProcessor processor = this.processorsCache
        .findProcessor(configId);
    if (processor == null)
    {
      throw new ProcessorException("Unsupported configuration");
    }
    else
    {
      if (processor.isVersionSupported(version))
      {
        return processor;
      }
      else
      {
        throw new ProcessorException("Unsupported version");
      }
    }
  }

  /* (non-Javadoc)
   * @see com.baxter.config.processor.ProcessorFactory#getRepository()
   */
  @Override
  public ConfigurationRepository getRepository()
  {
    return this.repository;
  }

  /**
   * Reads available processor descriptors and loads them.
   */
  private void loadProcessors()
  {
    try
    {
      LOGGER.debug("Looking for available processors in classpath...");
      final Enumeration<URL> descriptorResources = Thread.currentThread()
          .getContextClassLoader().getResources(DESCRIPTOR_RESOURCE);
      if (!descriptorResources.hasMoreElements())
      {
        LOGGER.warn("Could not find any processor in classpath");
      }
      while (descriptorResources.hasMoreElements())
      {
        final URL descriptorResource = descriptorResources.nextElement();
        try
        {
          final Descriptor descriptor = Loader.getInstance().load(
              descriptorResource);
          LOGGER.debug("Found available processor - {}", descriptor);

          try
          {
            final Descriptor existingDescriptor = this.repository
                .getDescriptor(descriptor.getProductId());
            // Now check if this descriptor is newer than the descriptor stored
            // in the repository
            // if this is newer one then apply update
            final Version existingVersion = Version.valueOf(existingDescriptor
                .getVersion());
            final Version availableVersion = Version.valueOf(descriptor
                .getVersion());
            if (existingVersion.compareTo(availableVersion) < 0)
            {
              final Upgrade upgrade = descriptor
                  .getLatestUpgrade(existingDescriptor.getVersion());
              if (upgrade != null)
              {
                this.repository.upgradePackage(descriptor, upgrade, this);
              }
              else
              {
                LOGGER.warn("Cannot find upgrade from {} to {}",
                    existingVersion, availableVersion);
              }
            }
            else
            {
              LOGGER.debug("Processor in repository is up to date - {}",
                  existingDescriptor);
            }
          }
          catch (final RepositoryException e)
          {
            // if there is no descriptor in repository then just copy entire
            // package
            LOGGER.info("Could not find {} in repository", descriptor);
            this.repository.installPackage(descriptor);
          }

          // Finally create processors and register them with cache
          for (final Processor processorDescriptor : descriptor.getProcessors())
          {
            final AbstractProcessor processor = createProcessor(descriptor,
                processorDescriptor);
            this.processorsCache.registerProcessor(descriptor.getProductId(),
                processorDescriptor.getConfigurationType(), processor);
          }

        }
        catch (final ProcessorException e)
        {
          LOGGER.error("Could not load descriptor from " + descriptorResource,
              e);
        }
      }
    }
    catch (final IOException e)
    {
      LOGGER.error("Failed when looking for descriptors", e);
    }
  }

  /**
   * Instantiates the processor.
   * 
   * @param descriptor
   *          package descriptor
   * @param processorDescriptor
   *          process descriptor
   * @return processor instance
   * @throws ProcessorException
   *           if cannot create processor for any reason
   */
  private AbstractProcessor createProcessor(final Descriptor descriptor,
      final Processor processorDescriptor) throws ProcessorException
  {
    LOGGER.trace("Creating processor - {}", processorDescriptor);
    try
    {
      final Class<? extends AbstractProcessor> processorClass = Class.forName(
          processorDescriptor.getClassName()).asSubclass(
          AbstractProcessor.class);
      final Constructor<? extends AbstractProcessor> processorConstructor = processorClass
          .getConstructor(Descriptor.class, ProcessorFactory.class);
      final AbstractProcessor processor = processorConstructor.newInstance(
          descriptor, this);
      for (final Parameter parameter : processorDescriptor.getParameters())
      {
        LOGGER.trace("Setting {} in {}", parameter, processorDescriptor);
        BUB.setProperty(processor, parameter.getName(), parameter.getValue());
      }
      return processor;
    }
    catch (final ClassNotFoundException e)
    {
      LOGGER.error("Could not find processor class", e);
      throw new ProcessorException(e);
    }
    catch (final NoSuchMethodException e)
    {
      LOGGER.error("Could not find constructor in processor class", e);
      throw new ProcessorException(e);
    }
    catch (final InvocationTargetException e)
    {
      LOGGER.error("Processor constructor failed", e);
      throw new ProcessorException(e);
    }
    catch (final IllegalAccessException e)
    {
      LOGGER.error("Processor constructor not accessible", e);
      throw new ProcessorException(e);
    }
    catch (final InstantiationException e)
    {
      LOGGER.error("Could not instantiate processor", e);
      throw new ProcessorException(e);
    }
  }

}
