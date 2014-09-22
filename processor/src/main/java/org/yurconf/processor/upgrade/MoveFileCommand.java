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
	try
	{
	  final List<String> filenames = listFilenames(repoDir.toURI());

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
		if (isFileNameMaskWildcard() && !isTargetDirectory) // if we have a wildcard in file attribute, then to shall be a directory
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
	catch (final IOException | URISyntaxException e)
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
