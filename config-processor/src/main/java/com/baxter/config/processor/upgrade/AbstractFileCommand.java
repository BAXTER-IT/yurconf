package com.baxter.config.processor.upgrade;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baxter.config.processor.desc.FilenameProvider;
import com.baxter.config.processor.util.URLLister;

/**
 * @author xpdev
 * @since ${developmentVersion}
 */
abstract class AbstractFileCommand
{

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final String filenameMask;

  private final Pattern filenamePattern;

  private final boolean isWildcardMask;

  protected AbstractFileCommand(final FilenameProvider filenameProvider)
  {
	this.filenameMask = filenameProvider.getFileNameMask();
	if (filenameMask != null)
	{
	  isWildcardMask = filenameMask.contains("*") || filenameMask.contains("?");
	}
	else
	{
	  isWildcardMask = false;
	}

	if (filenameProvider.getFileNamePattern() != null)
	{
	  this.filenamePattern = Pattern.compile(filenameProvider.getFileNamePattern());
	}
	else
	{
	  this.filenamePattern = null;
	}
  }

  protected boolean isFilenamePatternEffective()
  {
	return (this.filenamePattern != null);
  }

  protected Pattern getFilenamePattern()
  {
	return filenamePattern;
  }

  protected boolean isFileNameMaskWildcard()
  {
	return isWildcardMask;
  }

  protected List<String> listFilenames(final URL baseURL) throws IOException
  {
	final URLLister urlLister = URLLister.getInstance(baseURL);
	if (isFilenamePatternEffective())
	{
	  return urlLister.list(baseURL, this.filenamePattern);
	}
	else
	{
	  return urlLister.list(baseURL, this.filenameMask);
	}
  }

}
