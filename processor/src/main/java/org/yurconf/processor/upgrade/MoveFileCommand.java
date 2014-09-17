/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package org.yurconf.processor.upgrade;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.yurconf.processor.desc.FilenameProvider;

/**
 * @author xpdev
 * @since 1.5
 */
class MoveFileCommand extends AbstractFileCommand implements UpgradeCommand
{

  private final String to;

  protected MoveFileCommand(final FilenameProvider filenameProvider, final String to)
  {
	super(filenameProvider);
	this.to = to;
  }

  @Override
  public void upgrade(final UpgradeContext context) throws UpgradeException
  {

	// Processor's repository root
	final File repoDir = context.getProcessorFactory().getRepository()
	    .getProductDirectory(context.getDescriptor().getProductId());
	final URL baseURL;
	try
	{
	  baseURL = repoDir.toURI().toURL();
	}
	catch (final MalformedURLException e)
	{
	  throw new UpgradeException(e);
	}
	try
	{
	  final List<String> filenames = listFilenames(baseURL);

	  if (isFilenamePatternEffective())
	  {

		for (String filename : filenames)
		{
		  final File file = new File(repoDir, filename);
		  if (isFilenamePatternEffective())
		  {
			final String newName = filename.replaceAll(getFilenamePattern().pattern(), this.to);
			final File targetFile = new File(repoDir, newName);
			replaceWithCheckExistingFile(file, targetFile);
		  }
		}
	  }
	  else
	  {
		boolean isTargetDirectory = this.to.endsWith("/");
		final File target = new File(repoDir, this.to);
		// if we have a wildcard in file attribute, then to shall be a directory
		if (isFileNameMaskWildcard() && !isTargetDirectory)
		{
		  throw new UpgradeException("Target is not a directory " + target.getAbsolutePath());
		}
		for (String filename : filenames)
		{
		  final File file = new File(repoDir, filename);

		  if (isTargetDirectory)
		  {
			File targetFile = new File(target, file.getName());
			replaceWithCheckExistingFile(file, targetFile);
		  }
		  else
		  {
			replaceWithCheckExistingFile(file, target);
		  }
		}
	  }
	}
	catch (final IOException e)
	{
	  throw new UpgradeException(e);
	}
  }

  private void replaceWithCheckExistingFile(final File file, final File targetFile) throws IOException
  {
	if (targetFile.exists())
	{
	  FileUtils.forceDelete(targetFile);
	  logger.warn("Target file will be overwritten {}", targetFile.getAbsolutePath());
	}
	FileUtils.moveFile(file, targetFile);
  }

}
