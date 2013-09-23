/**
 * 
 */
package com.baxter.config.processor.repo.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baxter.config.processor.ConfigurationRepository;
import com.baxter.config.processor.ProcessorException;
import com.baxter.config.processor.ProcessorFactory;
import com.baxter.config.processor.desc.AbstractUpgradeFile;
import com.baxter.config.processor.desc.Descriptor;
import com.baxter.config.processor.desc.Loader;
import com.baxter.config.processor.desc.Upgrade;
import com.baxter.config.processor.repo.RepositoryException;
import com.baxter.config.processor.upgrade.CommandFactory;
import com.baxter.config.processor.upgrade.UpgradeContext;
import com.baxter.config.processor.util.URLLister;

/**
 * The Configuration Repository manager.
 * 
 * @author xpdev
 * @since ${developmentVersion}
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

  RepositoryImpl(final File root) throws RepositoryException
  {
	this.root = root;
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

  @Override
  public void installPackage(final Descriptor descriptor) throws IOException, ProcessorException
  {
	LOGGER.info("Installing {} in repository", descriptor);
	final URLLister urlLister = URLLister.getInstance(descriptor.getSourceUrl());
	final List<String> entryPaths = urlLister.list(descriptor.getSourceUrl());

	final File productDirectory = getProductDirectory(descriptor.getProductId());

	// Perform default configuration copying
	for (final String filePath : entryPaths)
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
	final File descriptorFile = new File(getProductDirectory(productId), TARGET_DESCRIPTOR_FILENAME);
	if (descriptorFile.isFile())
	{
	  try
	  {
		return Loader.getInstance().load(descriptorFile.toURI().toURL());
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

  @Override
  public Iterator<Descriptor> getDescriptors() throws ProcessorException
  {
	try
	{
	  return new DescriptorsIterator();
	}
	catch (final IOException e)
	{
	  throw new ProcessorException("Failed to iterate the processors", e);
	}
  }

}
