package com.baxter.config.servlet;

public enum ConfigurationType
{
  properties
  {
  },

  log4j
  {

  },
  ;

  public String getContentType()
  {
	return "text/xml";
  }
}
