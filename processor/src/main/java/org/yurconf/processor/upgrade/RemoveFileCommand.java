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
class RemoveFileCommand extends AbstractFileCommand implements UpgradeCommand
{

  protected RemoveFileCommand(final FilenameProvider filenameProvider)
  {
	super(filenameProvider);
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
	  for (String filename : filenames)
	  {
		final File file = new File(repoDir, filename);
		FileUtils.deleteQuietly(file);
	  }
	}
	catch (final IOException e)
	{
	  throw new UpgradeException(e);
	}

  }

}
