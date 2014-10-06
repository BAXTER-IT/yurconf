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

import org.kohsuke.args4j.CmdLineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yura
 * @since 1.5
 */
public class Main
{

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  /**
   * Hidden constructor.
   */
  private Main()
  {
  }

  /**
   * Entry point for Yurconf Server.
   * @param args command line arguments
   */
  public static void main(String[] args)
  {
	final long startTime = System.currentTimeMillis();

	LOGGER.info("Preparing to launch Yurconf Server");
	try
	{
	  final CmdParameters p = CmdParameters.parse(args);

	  final InetSocketAddress serverAddress = new InetSocketAddress(p.getHost(), p.getPort());
	  final ProcessorsClassLoader classLoader = ProcessorsClassLoader.createInstance(p.getLocations());
	  final Server server = new Server(serverAddress).configureContext(p.getContext(), classLoader);

	  LOGGER.info("Launching Yurconf Server");
	  server.start();
	  final long launchTime = System.currentTimeMillis() - startTime;
	  LOGGER.info("Yurconf Server started");
	  System.out.printf("Yurconf Server started in %1$d ms", launchTime);

	  try
	  {
		server.join();
		LOGGER.info("Yurconf Server has finished :)");
	  }
	  catch (final InterruptedException e)
	  {
		LOGGER.warn("Yurconf Server has been interrupted");
	  }

	}
	catch (final CmdLineException e)
	{
	  LOGGER.error("Bad parameters", e);

	  System.exit(1);
	}
	catch (final Exception e)
	{
	  LOGGER.error("Yurconf Server failed to start", e);
	  System.err.println("Failed to start Yurconf Server");
	  e.printStackTrace(System.err);
	  System.exit(1);
	}

  }

}
