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

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import org.junit.Test;

public class TestServer
{

  @Test
  public void startAndShutdown() throws Exception
  {
	Properties properties = new Properties(System.getProperties());
	try
	{
	  System.setProperty("yurconf.host", "localhost");
	  System.setProperty("yurconf.port", "4567");
	  System.setProperty("yurconf.context", "/yc");
	  System.setProperty("yurconf.processors", "./src/test/fs/processors");
	  System.setProperty("yurconf.repository", "./target/test-repository");
	  final Server srv = new Server();
	  srv.start();
	  for (int i = 0; i < 10 & !srv.isStarted(); i++)
	  {
		synchronized (srv)
		{
		  srv.wait(1000);
		}
	  }
	  final URL ping = new URL("http://localhost:4567/yc/ping?origin=junit");
	  try ( final InputStream s = ping.openStream() ) {
		final BufferedReader r = new BufferedReader( new InputStreamReader( s));
		final String pong = r.readLine();
		assertTrue( "No expected pong", pong.startsWith("Ping from junit arrived on ") );
	  }
	  srv.stop();
	}
	finally
	{
	  System.setProperties(properties);
	}
  }
}
