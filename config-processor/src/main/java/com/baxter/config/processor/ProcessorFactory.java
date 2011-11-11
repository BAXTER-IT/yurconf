/**
 * 
 */
package com.baxter.config.processor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
 * @TODO extract the repository related functionality into the new ConfigurationRepository class.
 * @TODO extract the package introspection into the new class
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

  private static final String TARGET_DESCRIPTOR_FILENAME = ".descriptor.xml";

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
	LOGGER.info("Creating factory with repository at {}", repository.getAbsolutePath());
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
	try
	{
	  FileUtils.forceMkdir(this.repository);
	}
	catch (final IOException e)
	{
	  throw new ProcessorException(e);
	}
  }

  /**
   * Reads available processor descriptors and loads them.
   */
  private void loadProcessors()
  {
	try
	{
	  final Enumeration<URL> descriptorResources = Thread.currentThread().getContextClassLoader().getResources(DESCRIPTOR_RESOURCE);
	  while (descriptorResources.hasMoreElements())
	  {
		final URL descriptorResource = descriptorResources.nextElement();
		try
		{
		  final Descriptor descriptor = Loader.getInstance().load(descriptorResource);
		  LOGGER.debug("Found available processor - {}", descriptor);

		  // if there is no descriptor in repository then just copy entire package
		  final File existingDescriptorFile = new File(getProductDirectory(descriptor.getProductId()), TARGET_DESCRIPTOR_FILENAME);
		  if (!existingDescriptorFile.exists())
		  {
			LOGGER.info("Could not find {} in repository", descriptor);
			copyPackageToRepo(descriptor);
		  }
		  else
		  {
			// Now check if this descriptor is newer than the descriptor stored in the repository
			// if this is newer one then apply update
			final Descriptor existingDescriptor = Loader.getInstance().load(existingDescriptorFile.toURI().toURL());
			final Version existingVersion = Version.valueOf(existingDescriptor.getVersion());
			final Version availableVersion = Version.valueOf(descriptor.getVersion());
			if (existingVersion.compareTo(availableVersion) < 0)
			{
			  // processor in JAR is newer than processor in Repo
			  LOGGER.info("Processor in repository is old - {}. Will upgrade...", existingDescriptor);
			}
			else
			{
			  LOGGER.debug("Processor in repository is up to date - {}", existingDescriptor);
			}

		  }

		  // Finally create processors and register them with cache
		  for (Processor processorDescriptor : descriptor.getProcessors())
		  {
			final AbstractProcessor processor = createProcessor(descriptor, processorDescriptor);
			this.processorsCache.registerProcessor(descriptor.getProductId(), processorDescriptor.getConfigurationType(), processor);
		  }

		}
		catch (final ProcessorException e)
		{
		  LOGGER.error("Could not load descriptor from " + descriptorResource, e);
		}
	  }
	}
	catch (final IOException e)
	{
	  LOGGER.error("Failed when looking for descriptors", e);
	}
  }

  /**
   * Copies all necessary resources from processor package to a local repository.
   * 
   * @param descriptor
   *          the processor descriptor
   * @throws IOException
   *           if failed to copy a resource
   */
  private void copyPackageToRepo(final Descriptor descriptor) throws IOException, ProcessorException
  {
	LOGGER.info("Installing {} in repository", descriptor);
	final URLLister urlLister = URLLister.getInstance(descriptor.getSourceUrl());
	final List<String> entryPaths = urlLister.list(descriptor.getSourceUrl());

	final File productDirectory = getProductDirectory(descriptor.getProductId());

	// Perform default configuration copying
	for (String filePath : entryPaths)
	{
	  final URL resourceURL = new URL(descriptor.getSourceUrl(), filePath);
	  final InputStream resourceStream = resourceURL.openStream();
	  try
	  {
		final File repositoryFile = new File(productDirectory, filePath);
		FileUtils.copyInputStreamToFile(resourceStream, repositoryFile);
	  }
	  finally
	  {
		resourceStream.close();
	  }
	}

	// Copy processor descriptor to repository
	final InputStream descriptorStream = descriptor.getUrl().openStream();
	try
	{
	  final File targetDescriptor = new File(productDirectory, TARGET_DESCRIPTOR_FILENAME);
	  FileUtils.copyInputStreamToFile(descriptorStream, targetDescriptor);
	}
	finally
	{
	  descriptorStream.close();
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
	LOGGER.trace("Creating processor - {}", processorDescriptor);
	try
	{
	  final Class<? extends AbstractProcessor> processorClass = Class.forName(processorDescriptor.getClassName()).asSubclass(
		  AbstractProcessor.class);
	  final Constructor<? extends AbstractProcessor> processorConstructor = processorClass.getConstructor(Descriptor.class);
	  final AbstractProcessor processor = processorConstructor.newInstance(descriptor);
	  processor.setFactory(this);
	  for (Parameter parameter : processorDescriptor.getParameters())
	  {
		LOGGER.trace("Setting {} in {}", parameter, processorDescriptor);
		// TODO refactor to bean properties
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

  /**
   * Returns the root directory for specified product.
   * 
   * @param productId
   *          product identifier
   * @return directory
   */
  File getProductDirectory(final String productId)
  {
	final String productPath = productId.replace('.', File.separatorChar);
	return new File(this.repository, productPath);
  }

}
