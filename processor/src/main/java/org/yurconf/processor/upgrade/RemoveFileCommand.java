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
import java.net.URISyntaxException;
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
	try
	{
	  final List<String> filenames = listFilenames(repoDir.toURI());
	  for (String filename : filenames)
	  {
		final File file = new File(repoDir, filename);
		FileUtils.deleteQuietly(file);
	  }
	}
	catch (final IOException | URISyntaxException e)
	{
	  throw new UpgradeException(e);
	}

  }

}
