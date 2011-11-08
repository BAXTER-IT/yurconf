package com.baxter.config.servlet;

public enum ConfigurationType
{
  properties
  {
	public boolean isVersionAddicted()
	{
	  return true;
	}
  },

  log4j
  {

  },
  ;

  public String getContentType()
  {
	return "text/xml";
  }

  public boolean isVersionAddicted()
  {
	return false;
  }
}
