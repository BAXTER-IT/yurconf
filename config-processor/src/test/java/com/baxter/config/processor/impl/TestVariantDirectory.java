/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.processor.impl;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

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
