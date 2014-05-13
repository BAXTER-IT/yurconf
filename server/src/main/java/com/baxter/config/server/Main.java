/*
 * Baxter Configuration Server
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.server;

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
   * Entry point for Configuration Server.
   * @param args command line arguments
   */
  public static void main(String[] args)
  {
	final long startTime = System.currentTimeMillis();

	LOGGER.info("Starting Baxter Configuration Server");
	final Server server = new Server();

	try
	{
	  server.start();
	  final long launchTime = System.currentTimeMillis() - startTime;
	  LOGGER.info("Baxter Configuration Server started");
	  System.out.printf("Baxter Configuration Server started in %1$d ms", launchTime);
	}
	catch (final Exception e)
	{
	  LOGGER.info("Baxter Configuration Server failed to start", e);
	  System.err.println("Failed to start server");
	  e.printStackTrace(System.err);
	  System.exit(1);
	}

	try
	{
	  server.join();
	}
	catch (final InterruptedException e)
	{
	  LOGGER.warn("Baxter Configuration Server has been interrupted");
	}

  }

}
