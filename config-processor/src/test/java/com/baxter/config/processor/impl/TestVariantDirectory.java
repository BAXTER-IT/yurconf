package com.baxter.config.processor.impl;

import java.io.File;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestVariantDirectory
{

  private final File ROOT = new File(".").getAbsoluteFile();

  @Test
  void checkNullVariant()
  {
	final File f = new VariantDirectory(ROOT, "mydir/", null);
	final File ef = new File(ROOT, "mydir/");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  void checkVariant()
  {
	final File f = new VariantDirectory(ROOT, "mydir/", "proba");
	final File ef = new File(ROOT, "mydir(proba)/");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  void checkVariant2()
  {
	final File f = new VariantDirectory(ROOT, "mydir", "proba");
	final File ef = new File(ROOT, "mydir(proba)");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }
}