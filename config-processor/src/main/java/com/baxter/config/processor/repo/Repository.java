/**
 * 
 */
package com.baxter.config.processor.repo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baxter.config.processor.ProcessorException;
import com.baxter.config.processor.ProcessorFactory;
import com.baxter.config.processor.desc.AbstractUpgradeFile;
import com.baxter.config.processor.desc.Descriptor;
import com.baxter.config.processor.desc.Loader;
import com.baxter.config.processor.desc.Upgrade;
import com.baxter.config.processor.upgrade.CommandFactory;
import com.baxter.config.processor.upgrade.UpgradeContext;
import com.baxter.config.processor.util.URLLister;

/**
 * The Configuration Repository manager.
 * 
 * @author xpdev
 * @since ${developmentVersion}
 */
public class Repository
{

  /**
   * Logger instance.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(Repository.class);

  /**
   * Descriptor file name in repository.
   */
  private static final String TARGET_DESCRIPTOR_FILENAME = ".descriptor.xml";

  /**
   * Configuration repository root.
   */
  private final File root;

  private Repository(final File root) throws RepositoryException
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

  public static Repository getInstance(final File root) throws RepositoryException
  {
	return new Repository(root);
  }

  public File getRoot()
  {
	return root;
  }

  /**
   * Returns the root directory for specified product.
   * 
   * @param productId
   *          product identifier
   * @return directory
   */
  public File getProductDirectory(final String productId)
  {
	final String productPath = productId.replace('.', File.separatorChar);
	return new File(getRoot(), productPath);
  }

  /**
   * Upgrades the package in repository.
   * 
   * @param descriptor
   *          the package descriptor
   * @param upgrade
   *          upgrade to execute
   * @throws ProcessorException
   *           if failed to upgrade
   */
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
	  for (AbstractUpgradeFile command : upgradeCommands)
	  {
		CommandFactory.getInstance().getCommand(command).upgrade(upgradeContext);
	  }
	}
	try
	{
	  installPackageDescriptor(descriptor);
	}
	catch (IOException e)
	{
	  LOGGER.error("Failed to install package descriptor", e);
	  throw new ProcessorException(e);
	}
  }

  /**
   * Install a configuration processor package into repository. Copies all necessary resources from processor package to a local
   * repository.
   * 
   * @param descriptor
   *          the processor descriptor
   * @throws IOException
   *           if failed to copy a resource
   */
  public void installPackage(final Descriptor descriptor) throws IOException, ProcessorException
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

  /**
   * Loads a product processors descriptor from this repository.
   * 
   * @param productId
   *          product identifier
   * @return processors descriptor
   * @throws ProcessorException
   *           if failed to load the descriptor from file
   * @throws RepositoryException
   *           if the descriptor file has not been found in repository
   */
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

}
