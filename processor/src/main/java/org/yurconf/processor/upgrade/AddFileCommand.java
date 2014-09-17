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
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.yurconf.processor.desc.FilenameProvider;

/**
 * @author xpdev
 * @since 1.5
 */
class AddFileCommand extends AbstractFileCommand implements UpgradeCommand
{

  AddFileCommand(final FilenameProvider filenameProvider)
  {
	super(filenameProvider);
  }

  @Override
  public void upgrade(final UpgradeContext context) throws UpgradeException
  {
	// Processor's repository root
	final File destDir = context.getProcessorFactory().getRepository()
	    .getProductDirectory(context.getDescriptor().getProductId());
	// the URL to processor's directory with default sources
	final URL sourceBase = context.getDescriptor().getSourceUrl();
	try
	{
	  final List<String> entryPaths = listFilenames(sourceBase);
	  for (String filename : entryPaths)
	  {
		final File destFile = new File(destDir, filename);
		if (destFile.isFile())
		{
		  logger.warn("Target file will be overwritten {}", destFile.getAbsolutePath());
		}
		FileUtils.copyURLToFile(new URL(sourceBase, filename), destFile);
	  }
	}
	catch (final IOException e)
	{
	  throw new UpgradeException(e);
	}
  }

}
