/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.processor.upgrade;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
	final URI sourceBase = context.getDescriptor().getDefaultSourceUri();
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
		FileUtils.copyURLToFile(sourceBase.resolve(filename).toURL(), destFile);
	  }
	}
	catch (final IOException | URISyntaxException e)
	{
	  throw new UpgradeException(e);
	}
  }

}
