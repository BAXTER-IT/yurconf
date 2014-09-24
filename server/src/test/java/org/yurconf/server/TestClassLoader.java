/*
 * Yurconf Server
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.server;

import java.net.URI;
import java.net.URL;
import java.util.Enumeration;

import org.junit.Test;
import org.yurconf.server.ProcessorsClassLoader;

public class TestClassLoader
{

  @Test
  public void loadJars() throws Exception
  {
	final ProcessorsClassLoader cl = ProcessorsClassLoader.createInstance("./src/test/fs/processors");
	final Enumeration<URL> resources = cl.getResources("META-INF/org.yurconf.processor.xml");
	while (resources.hasMoreElements())
	{
	  final URL resource = resources.nextElement();
	  System.out.println("==============\n" + resource);
	  final URI uri = resource.toURI();
	  final URL pURL = new URL( resource, ".." ).toURI().normalize().toURL();
	  System.out.println(pURL);
	}
  }

}
