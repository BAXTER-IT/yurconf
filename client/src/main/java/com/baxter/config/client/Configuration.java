/*
 * Baxter Configuration Client
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.client;

import java.net.URL;

/**
 * Facade to configuration. Use this object to query all the configurations you need.
 *
 * @author ykryshchuk
 * @since 1.5
 */
public final class Configuration
{

  private static final Configuration INSTANCE = new Configuration(DefaultEnvironment.getInstance());

  private final Environment environment;

  private Configuration(final Environment environment)
  {
	this.environment = environment;
  }

  public static Configuration getInstance()
  {
	return INSTANCE;
  }

  public Environment getEnvironment()
  {
	return this.environment;
  }

  /**
   * Determines if the configuration server has been specified.
   *
   * @return true if the configuration server has been specified
   */
  public boolean isOnline()
  {
	final URL restUrl = getEnvironment().getRestUrl();
	if (restUrl != null)
	{
	  // Ideally we should also perform some ping....
	  return true;
	}
	return false;
  }

}
