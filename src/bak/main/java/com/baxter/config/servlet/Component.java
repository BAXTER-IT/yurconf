/**
 * 
 */
package com.baxter.config.servlet;

/**
 * @author ykryshchuk
 * 
 */
public enum Component
{

  // Server
  dbserver(true)
  {
  },
  broadcast(true)
  {
  },
  blotterserver(true)
  {
  },
  validator(true)
  {
  },
  twserver(true)
  {
  },
  fwdcontroller(true)
  {
  },
  fwdircontroller(true)
  {
  },
  // Client
  admintool(false)
  {
  },
  blotter(false)
  {
  },
  trackwheel(false)
  {
  },
  fwdmonitor(false)
  {
  },
  ;

  private boolean isServer;

  private Component(boolean isServer)
  {
	this.isServer = isServer;
  }

  public String getFileName(final ConfigurationType type)
  {
	switch (type)
	{
	case properties:
	  return "properties.xml";
	case log4j:
	  return "log4j-" + name() + ".xml";
	default:
	  throw new IllegalArgumentException("Unsupported config type");
	}
  }

  public boolean isServer()
  {
	return isServer;
  }
}
