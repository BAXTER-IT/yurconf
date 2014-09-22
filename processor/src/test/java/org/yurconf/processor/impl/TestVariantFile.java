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
import org.yurconf.processor.impl.VariantFile;

/**
 * @author ykryshchuk
 * @since 1.5
 */
public class TestVariantFile
{

  private final File ROOT = new File(".").getAbsoluteFile();

  @Test
  public void checkNullVariant()
  {
	final File f = new VariantFile(ROOT, "myfile.txt", null);
	final File ef = new File(ROOT, "myfile.txt");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  public void checkPathNullVariant()
  {
	final File f = new VariantFile(ROOT, "dir/subdir/myfile.txt", null);
	final File ef = new File(ROOT, "dir/subdir/myfile.txt");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  public void checkVariant()
  {
	final File f = new VariantFile(ROOT, "myfile.txt", "test");
	final File ef = new File(ROOT, "myfile(test).txt");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  public void checkVariantNoExt()
  {
	final File f = new VariantFile(ROOT, "myfile", "test");
	final File ef = new File(ROOT, "myfile(test)");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  public void checkPathVariant()
  {
	final File f = new VariantFile(ROOT, "dir/subdir/myfile.txt", "test");
	final File ef = new File(ROOT, "dir/subdir/myfile(test).txt");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  public void checkPathExtVariant()
  {
	final File f = new VariantFile(ROOT, "dir/sub.dir/myfile.txt", "test");
	final File ef = new File(ROOT, "dir/sub.dir/myfile(test).txt");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

  @Test
  public void checkPathExtVariantNoExt()
  {
	final File f = new VariantFile(ROOT, "dir/sub.dir/myfile", "test");
	final File ef = new File(ROOT, "dir/sub.dir/myfile(test)");
	assertEquals(ef.getAbsolutePath(), f.getAbsolutePath());
  }

}
