/**
 * 
 */
package com.baxter.config.processor.impl;

import java.io.File;

/**
 * Variant file.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
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
