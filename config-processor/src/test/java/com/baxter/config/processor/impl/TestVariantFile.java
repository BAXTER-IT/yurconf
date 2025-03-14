/**
 * 
 */
package com.baxter.config.processor.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.Test;

/**
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
class TestVariantFile
{

  private final File ROOT = new File(".").getAbsoluteFile();

  @Test
  void checkNullVariant()
  {
	final File f = new VariantFile(ROOT, "myfile.txt", null);
	final File ef = new File(ROOT, "myfile.txt");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  void checkPathNullVariant()
  {
	final File f = new VariantFile(ROOT, "dir/subdir/myfile.txt", null);
	final File ef = new File(ROOT, "dir/subdir/myfile.txt");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  void checkVariant()
  {
	final File f = new VariantFile(ROOT, "myfile.txt", "test");
	final File ef = new File(ROOT, "myfile(test).txt");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  void checkVariantNoExt()
  {
	final File f = new VariantFile(ROOT, "myfile", "test");
	final File ef = new File(ROOT, "myfile(test)");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  void checkPathVariant()
  {
	final File f = new VariantFile(ROOT, "dir/subdir/myfile.txt", "test");
	final File ef = new File(ROOT, "dir/subdir/myfile(test).txt");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  void checkPathExtVariant()
  {
	final File f = new VariantFile(ROOT, "dir/sub.dir/myfile.txt", "test");
	final File ef = new File(ROOT, "dir/sub.dir/myfile(test).txt");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  void checkPathExtVariantNoExt()
  {
	final File f = new VariantFile(ROOT, "dir/sub.dir/myfile", "test");
	final File ef = new File(ROOT, "dir/sub.dir/myfile(test)");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

}
