/**
 * 
 */
package com.baxter.config.processor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baxter.config.om.ConfigID;
import com.baxter.config.om.Version;
import com.baxter.config.processor.desc.Descriptor;
import com.baxter.config.processor.desc.Loader;
import com.baxter.config.processor.desc.Parameter;
import com.baxter.config.processor.desc.Processor;

/**
 * Processor Factory that returns a target processor for specified input.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class ProcessorFactory
{

  /**
   * Logger instance.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorFactory.class);

  /**
   * Name of the processor descriptor resource.
   */
  private static final String DESCRIPTOR_RESOURCE = "META-INF/services/com.baxter.config.processor.xml";

  /**
   * Configuration repository root.
   */
  private final File repository;

  /**
   * Processors cache.
   */
  private final ProcessorsCache processorsCache = new ProcessorsCache();

  /**
   * Hidden constructor.
   * 
   * @param repository
   *          configuration repository root
   * @throws ProcessorException
   *           if cannot create factory
   */
  private ProcessorFactory(final File repository) throws ProcessorException
  {
	LOGGER.info("Creating factory with repository at {}", this.repository);
	this.repository = repository;
	setupRepository();
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
  public static ProcessorFactory getInstance(final File repository) throws ProcessorException
  {
	return new ProcessorFactory(repository);
  }

  /**
   * Returns the processor for specified request. Each call to this method returns new instance of processor.
   * 
   * @param configId
   *          configuration identifier
   * @param version
   *          configuration version
   * @return the matched processor
   * @throws ProcessorException
   *           if cannot match the processor for specified input
   */
  public AbstractProcessor getProcessor(final ConfigID configId, final Version version) throws ProcessorException
  {
	final AbstractProcessor processor = this.processorsCache.findProcessor(configId);
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

  /**
   * Initializes the repository.
   * 
   * @throws ProcessorException
   *           if failed to initialize repository
   */
  private void setupRepository() throws ProcessorException
  {
	if (!this.repository.isDirectory())
	{
	  if (this.repository.isFile())
	  {
		LOGGER.error("The specified repository path denotes a FILE");
		throw new ProcessorException("Invalid Repository");
	  }
	  else
	  {
		if (!this.repository.mkdirs())
		{
		  LOGGER.error("Failed to create Repository directory");
		  throw new ProcessorException("Cannot create Repository");
		}
		else
		{
		  LOGGER.debug("Created Repository root directory");
		}
	  }
	}
	else
	{
	  LOGGER.debug("Repository root directory exists");
	}
  }

  /**
   * Reads available processor descriptors and loads them.
   */
  private void loadProcessors()
  {
	try
	{
	  final Enumeration<URL> descriptors = Thread.currentThread().getContextClassLoader().getResources(DESCRIPTOR_RESOURCE);
	  while (descriptors.hasMoreElements())
	  {
		final URL descriptorUrl = descriptors.nextElement();
		try
		{
		  final Descriptor descriptor = Loader.getInstance().load(descriptorUrl);
		  // TODO
		  // Now check if this descriptor is newer than the descriptor stored in the repository
		  // if there is no descriptor in repo then just copy default configuration and descriptor to repository
		  // if this is newer one then apply update

		  // Finally create processor and register it with cache
		  for (Processor p : descriptor.getProcessors())
		  {
			final AbstractProcessor processor = createProcessor(descriptor, p);
			this.processorsCache.registerProcessor(descriptor.getProductId(), p.getConfigurationType(), processor);
		  }
		}
		catch (final ProcessorException e)
		{
		  LOGGER.error("Could not load descriptor from " + descriptorUrl, e);
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
  private AbstractProcessor createProcessor(final Descriptor descriptor, final Processor processorDescriptor)
	  throws ProcessorException
  {
	try
	{
	  final Class<? extends AbstractProcessor> processorClass = Class.forName(processorDescriptor.getClassName()).asSubclass(
		  AbstractProcessor.class);
	  final Constructor<? extends AbstractProcessor> processorConstructor = processorClass.getConstructor(Descriptor.class);
	  final AbstractProcessor processor = processorConstructor.newInstance(descriptor);
	  processor.setFactory(this);
	  for (Parameter parameter : processorDescriptor.getParameters())
	  {
		processor.setParameter(parameter.getName(), parameter.getValue());
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
