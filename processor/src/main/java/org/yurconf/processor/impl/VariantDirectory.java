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
 * Variant directory.
 *
 * @author ykryshchuk
 * @since 1.5
 */
class VariantDirectory extends File
{
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public VariantDirectory(final File basedir, final String name, final String variant)
  {
	super(basedir, buildVariantName(name, variant));
  }

  private static String buildVariantName(final String name, final String variant)
  {
	if (variant == null)
	{
	  return name;
	}
	if (name.endsWith("/") || name.endsWith("\\"))
	{
	  return String.format("%1$s(%3$s)%2$s", name.substring(0, name.length() - 1), name.substring(name.length() - 1), variant);
	}
	else
	{
	  return String.format("%1$s(%2$s)", name, variant);
	}
  }

}
