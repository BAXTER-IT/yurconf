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

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.yurconf.processor.impl.VariantDirectory;

/**
 * @author ykryshchuk
 * @since 1.5
 */
public class TestVariantDirectory
{

  private final File ROOT = new File(".").getAbsoluteFile();

  @Test
  public void checkNullVariant()
  {
	final File f = new VariantDirectory(ROOT, "mydir/", null);
	final File ef = new File(ROOT, "mydir/");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  public void checkVariant()
  {
	final File f = new VariantDirectory(ROOT, "mydir/", "proba");
	final File ef = new File(ROOT, "mydir(proba)/");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  public void checkVariant2()
  {
	final File f = new VariantDirectory(ROOT, "mydir", "proba");
	final File ef = new File(ROOT, "mydir(proba)");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }
}
