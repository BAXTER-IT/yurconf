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

import java.net.InetSocketAddress;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yurconf.servlet.PingServlet;
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

  Server(final InetSocketAddress address)
  {
	super(address);
  }

  Server configureContext(final String path, final ClassLoader classLoader)
  {
	final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
	LOGGER.trace("Yurconf Server Context = {}", path);
	context.setContextPath(path);
	context.addServlet(PingServlet.class, "/ping");
	context.addServlet(ViewerServlet.class, "/view/*");
	context.addServlet(RestfulServlet.class, "/rest/*");
	context.setClassLoader(classLoader);
	context.addEventListener(new ProcessorFactoryInitializer(classLoader));
	setHandler(context);
	return this;
  }

}
