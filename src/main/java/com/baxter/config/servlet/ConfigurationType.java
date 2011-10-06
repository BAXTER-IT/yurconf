package com.baxter.config.servlet;

public enum ConfigurationType
{
  properties
  {
	public String getFileName(String componnentName)
	{
	  return name() + ".xml";
	};
  },

  log4j;

  public String getFileName(String componnentName)
  {
	return name() + "_" + componnentName + ".xml";
  }
  
  public String getContentType()
  {
	return "text/xml";
  }
}
