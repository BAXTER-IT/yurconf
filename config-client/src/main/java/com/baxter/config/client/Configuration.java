/**
 * 
 */
package com.baxter.config.client;


/**
 * Facade to configuration. Use this object to query all the configurations you need.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class Configuration
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

}
