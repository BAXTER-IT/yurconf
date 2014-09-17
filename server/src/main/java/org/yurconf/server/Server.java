/*
 * Baxter Configuration Server
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package org.yurconf.server;

import java.io.File;
import java.io.FilenameFilter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yurconf.servlet.ProcessorFactoryInitializer;
import org.yurconf.servlet.RestfulServlet;
import org.yurconf.servlet.ViewerServlet;

/**
 * Configuration Server class.
 *
 * @author yura
 * @since 1.5
 */
public class Server extends org.eclipse.jetty.server.Server
{

  private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

  private static final int DEFAULT_JETTY_PORT = 4040;

  private static final String DEFAULT_JETTY_HOST = "0.0.0.0";

  private final List<URL> jarUrls = new ArrayList<>();

  Server() {
	super(getServerSocketAddres());

	ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
	context.setContextPath("/config-server");
	context.addServlet(RestfulServlet.class, "/rest/*");
	context.addServlet(ViewerServlet.class, "/view/*");
	context.addEventListener(new ProcessorFactoryInitializer());
	context.setClassLoader(getProcessorsClassLoader());
	setHandler(context);
  }

  private static InetSocketAddress getServerSocketAddres() {
	final String jettyPortString = System.getProperty("jetty.port");
	LOGGER.debug("Jetty port value {}", jettyPortString);
	final int jettyPort = jettyPortString == null ? DEFAULT_JETTY_PORT : Integer.parseInt(jettyPortString);
	final String jettyHost = System.getProperty("jetty.host", DEFAULT_JETTY_HOST);
	LOGGER.info("Jetty will run on {}:{}", jettyHost, jettyPort);
	return new InetSocketAddress(jettyHost, jettyPort);
  }

  private ClassLoader getProcessorsClassLoader()
  {
	final String configProcessorsPath = System.getProperty("com.baxter.config.Processors");
	LOGGER.info("Configuration processors path: {}", configProcessorsPath);
	final File configProcessorsDir = new File(configProcessorsPath);
	registerProcessorJar(configProcessorsDir);
	for (File f : configProcessorsDir.listFiles(new ProcessorFilenameFilter()))
	{
	  registerProcessorJar(f);
	}
	return new URLClassLoader(jarUrls.toArray(new URL[jarUrls.size()]), ClassLoader.getSystemClassLoader());
  }

  private void registerProcessorJar(final File f)
  {
	try
	{
	  final URL jarUrl = f.toURI().toURL();
	  LOGGER.debug("JAR {} as {}", f, jarUrl);
	  jarUrls.add(jarUrl);
	}
	catch (final MalformedURLException e)
	{
	  LOGGER.warn("Cannot get URL for {}", f, e);
	}
  }

  private class ProcessorFilenameFilter implements FilenameFilter
  {

	@Override
	public boolean accept(final File dir, final String name)
	{
	  return name.endsWith(".jar");
	}

  }

}
