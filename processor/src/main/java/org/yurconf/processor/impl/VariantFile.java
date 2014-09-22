/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.processor.impl;

import java.io.File;

/**
 * Variant file.
 *
 * @author ykryshchuk
 * @since 1.5
 */
class VariantFile extends File
{
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public VariantFile(final File basedir, final String name, final String variant)
  {
	super(basedir, buildVariantName(name, variant));
  }

  private static String buildVariantName(final String name, final String variant)
  {
	if (variant == null)
	{
	  return name;
	}
	final int lastDot = name.lastIndexOf('.');
	final int lastPath = Math.max(name.lastIndexOf('\\'), name.lastIndexOf('/'));
	if (lastDot == -1 || lastPath > lastDot)
	{
	  return String.format("%1$s(%2$s)", name, variant);
	}
	return String.format("%1$s(%3$s)%2$s", name.substring(0, lastDot), name.substring(lastDot), variant);
  }

}
