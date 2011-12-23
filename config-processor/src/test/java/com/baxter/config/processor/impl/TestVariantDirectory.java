/**
 * 
 */
package com.baxter.config.processor.impl;

import java.io.File;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * @author ykryshchuk
 * @since ${developmentVersion}
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
