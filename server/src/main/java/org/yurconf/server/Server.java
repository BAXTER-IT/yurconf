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

  private static final String SYSP_HOST = "yurconf.host";
  private static final String SYSP_PORT = "yurconf.port";
  private static final String SYSP_CONTEXT = "yurconf.context";
  private static final String SYSP_PROCESSORS = "yurconf.processors";

  private static final String DEFAULT_HOST = "0.0.0.0";
  private static final int DEFAULT_PORT = 4040;
  private static final String DEFAULT_CONTEXT = "/";

  Server()
  {
	super(getServerSocketAddres());
	ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
	final String contextPath = System.getProperty(SYSP_CONTEXT, DEFAULT_CONTEXT);
	LOGGER.trace("Yurconf Server Context = {}", contextPath);
	context.setContextPath(contextPath);
	context.addServlet(RestfulServlet.class, "/rest/*");
	context.addServlet(ViewerServlet.class, "/view/*");
	context.addServlet(PingServlet.class, "/ping");
	final String processorsPath = System.getProperty(SYSP_PROCESSORS);
	LOGGER.trace("Yurconf Server Processors here {}", processorsPath);
	final ClassLoader classLoader = ClassLoader.createInstance(processorsPath);
	context.setClassLoader(classLoader);
	context.addEventListener(new ProcessorFactoryInitializer(classLoader));
	setHandler(context);
  }

  private static InetSocketAddress getServerSocketAddres()
  {
	final String portString = System.getProperty(SYSP_PORT);
	LOGGER.trace("Configured Yurconf Server port = {}", portString);
	final int port = portString == null ? DEFAULT_PORT : Integer.parseInt(portString);
	final String host = System.getProperty(SYSP_HOST, DEFAULT_HOST);
	LOGGER.info("Yurconf Server will run on {}:{}", host, port);
	return new InetSocketAddress(host, port);
  }

}
