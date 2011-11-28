/**
 * 
 */
package com.baxter.config.processor.upgrade;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.baxter.config.processor.desc.FilenameProvider;

/**
 * @author xpdev
 * @since ${developmentVersion}
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
	final File repoDir = context.getProcessorRepositoryRoot();
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
		if (isFilenamePatternEffective())
		{
		  final String newName = filename.replaceAll(getFilenamePattern().pattern(), this.to);
		  final File targetFile = new File(repoDir, newName);
		  FileUtils.moveFile(file, targetFile);
		}
		else
		{
		  final File target = new File(repoDir, this.to);
		  if (target.isDirectory())
		  {
			FileUtils.moveToDirectory(file, target, true);
		  }
		  else
		  {
			FileUtils.moveFile(file, target);
		  }
		}
	  }
	}
	catch (final IOException e)
	{
	  throw new UpgradeException(e);
	}
  }

}
