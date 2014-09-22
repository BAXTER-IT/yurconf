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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yurconf.processor.desc.FilenameProvider;
import org.yurconf.processor.util.UriLister;

/**
 * @author xpdev
 * @since 1.5
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

  protected List<String> listFilenames(final URI baseUri) throws URISyntaxException, IOException
  {
	final UriLister uriLister = UriLister.getInstance(baseUri);
	if (isFilenamePatternEffective())
	{
	  return uriLister.list(baseUri, this.filenamePattern);
	}
	else
	{
	  return uriLister.list(baseUri, this.filenameMask);
	}
  }

}
