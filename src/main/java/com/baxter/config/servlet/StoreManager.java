package com.baxter.config.servlet;


public class StoreManager
{
  public String getConfigurationURL(String configType, String componnentId){
	final String userHome = System.getProperty("user.home");
	final StringBuilder path = new StringBuilder();
	path.append(userHome).append("/");
	path.append("baxter-configuration").append("/");
	path.append(configType).append("/");
	path.append(configType);
	path.append(".xml");
	return path.toString();
  }
}
