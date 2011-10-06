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
  dbserver
  {
  },
  broadcast
  {
  },
  blotterserver
  {
  },
  validator
  {
  },
  twserver
  {
  },
  fwdcontroller
  {
  },
  fwdircontroller
  {
  },
  // Client
  admintool
  {
  },
  blotter
  {
  },
  trackwheel
  {
  },
  fwdmonitor
  {
  },
  ;

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
}
