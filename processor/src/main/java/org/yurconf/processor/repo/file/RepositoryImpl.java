/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 *
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 *
 * Join our team to help make it better.
 */
package org.yurconf.processor.repo.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yurconf.processor.ConfigurationRepository;
import org.yurconf.processor.ProcessorException;
import org.yurconf.processor.ProcessorFactory;
import org.yurconf.processor.desc.AbstractUpgradeFile;
import org.yurconf.processor.desc.Descriptor;
import org.yurconf.processor.desc.Loader;
import org.yurconf.processor.desc.Upgrade;
import org.yurconf.processor.repo.RepositoryException;
import org.yurconf.processor.upgrade.CommandFactory;
import org.yurconf.processor.upgrade.UpgradeContext;
import org.yurconf.processor.util.UriLister;

/**
 * The Configuration Repository manager.
 *
 * @author xpdev
 * @since 1.5
 */
final class RepositoryImpl implements ConfigurationRepository
{

  /**
   * Logger instance.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryImpl.class);

  /**
   * Descriptor file name in repository.
   */
  private static final String TARGET_DESCRIPTOR_FILENAME = ".descriptor.xml";

  /**=
   * Configuration repository root.
   */
  private final File root;

  private final ClassLoader processorsCL;

  private final Map<String, Descriptor> descriptorCache = new HashMap<>();

  RepositoryImpl(final File root, final ClassLoader processorsCL) throws RepositoryException
  {
	this.root = root;
	this.processorsCL = processorsCL;
	try
	{
	  FileUtils.forceMkdir(this.root);
	}
	catch (final IOException e)
	{
	  throw new RepositoryException(e);
	}
  }

  @Override
  public File getRoot()
  {
	return root;
  }

  @Override
  public File getProductDirectory(final String productId)
  {
	final String productPath = productId.replace('.', File.separatorChar);
	return new File(getRoot(), productPath);
  }

  @Override
  public void upgradePackage(final Descriptor descriptor, final Upgrade upgrade, final ProcessorFactory processorFactory)
	  throws ProcessorException
  {
	quoteForMutation(descriptor + " " + upgrade);
	final UpgradeContext upgradeContext = new UpgradeContext()
	{

	  @Override
	  public Descriptor getDescriptor()
	  {
		return descriptor;
	  }

	  @Override
	  public ProcessorFactory getProcessorFactory()
	  {
		return processorFactory;
	  }

	};
	final List<? extends AbstractUpgradeFile> upgradeCommands = upgrade.getCommands();
	if (upgradeCommands == null)
	{
	  LOGGER.info("No commands found for {}", upgrade);
	}
	else
	{
	  for (final AbstractUpgradeFile command : upgradeCommands)
	  {
		CommandFactory.getInstance().getCommand(command).upgrade(upgradeContext);
	  }
	}
	try
	{
	  installPackageDescriptor(descriptor);
	}
	catch (final IOException e)
	{
	  LOGGER.error("Failed to install package descriptor", e);
	  throw new ProcessorException(e);
	}
  }

  void quoteForMutation(final Object reason)
  {
	final File rootDir = getRoot();
	if (!rootDir.canWrite())
	{
	  LOGGER.error("Cannot quote for mutation: {}", reason);
	  System.exit(5);
	}
  }

  @Override
  public void installPackage(final Descriptor descriptor) throws IOException, ProcessorException
  {
	quoteForMutation("Installing " + descriptor);
	LOGGER.info("Installing {} in repository", descriptor);
	installPackageDescriptor(descriptor);
  }

  private void installPackageDescriptor(final Descriptor descriptor) throws IOException
  {
	final File productDirectory = getProductDirectory(descriptor.getProductId());
	// Copy processor descriptor to repository
	final InputStream descriptorStream = descriptor.getUrl().openStream();
	try
	{
	  final File targetDescriptor = new File(productDirectory, TARGET_DESCRIPTOR_FILENAME);
	  LOGGER.debug("Installing descrptor for {}", descriptor);
	  FileUtils.copyInputStreamToFile(descriptorStream, targetDescriptor);
	}
	finally
	{
	  descriptorStream.close();
	}
  }

  @Override
  public Descriptor getDescriptor(final String productId) throws ProcessorException
  {
	final Descriptor cached = descriptorCache.get(productId);
	if (cached == null)
	{
	  LOGGER.warn("No cached descriptor for {}", productId);
	  final File descriptorFile = new File(getProductDirectory(productId), TARGET_DESCRIPTOR_FILENAME);
	  if (descriptorFile.isFile())
	  {
		try
		{
		  final Descriptor descriptor = Loader.getInstance().load(descriptorFile.toURI().toURL());
		  descriptorCache.put(productId, descriptor);
		  return descriptor;
		}
		catch (final MalformedURLException e)
		{
		  throw new ProcessorException(e);
		}
	  }
	  else
	  {
		throw new RepositoryException("Descriptor not found");
	  }
	}
	else
	{
	  return cached;
	}
  }

  @Override
  public Iterator<Descriptor> getDescriptors() throws ProcessorException
  {
	try
	{
	  return new DescriptorsIterator(processorsCL);
	}
	catch (final IOException e)
	{
	  throw new ProcessorException("Failed to iterate the processors", e);
	}
  }

}
