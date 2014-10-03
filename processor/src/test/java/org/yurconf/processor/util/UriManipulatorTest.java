package org.yurconf.processor.util;

import java.net.URI;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UriManipulatorTest
{

  @Test
  public void resolveFileVsString() throws Exception
  {
	final URI base = URI.create("file:/opt/mydir/file.txt");
	final URI result = UriManipulator.resolve(base, "other.log");
	assertEquals(URI.create("file:/opt/mydir/other.log"), result);
  }

  @Test
  public void resolveJarFileVsString()throws Exception
  {
	final URI base = URI.create("jar:file:/opt/mydir/file.jar!/META-INF/resource.xml");
	final URI result = UriManipulator.resolve(base, "../");
	assertEquals(URI.create("jar:file:/opt/mydir/file.jar!/"), result);
  }

  @Test
  public void resolveJarFileVsAbsString()throws Exception
  {
	final URI base = URI.create("jar:file:/opt/mydir/file.jar!/META-INF/resource.xml");
	final URI result = UriManipulator.resolve(base, "/processor/");
	assertEquals(URI.create("jar:file:/opt/mydir/file.jar!/processor/"), result);
  }

}
