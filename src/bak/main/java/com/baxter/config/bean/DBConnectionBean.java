package com.baxter.config.bean;

import java.util.NoSuchElementException;

import com.baxter.config.model.properties.Group;
import com.baxter.config.model.properties.Properties;

public class DBConnectionBean
{
  private String dbFamily;
  private String dbHost;
  private String dbPort;
  private String dbName;
  private String dbPwd;

  public String getDbFamily()
  {
	return dbFamily;
  }

  public void setDbFamily(String dbFamily)
  {
	this.dbFamily = dbFamily;
  }

  public String getDbHost()
  {
	return dbHost;
  }

  public void setDbHost(String dbHost)
  {
	this.dbHost = dbHost;
  }

  public String getDbPort()
  {
	return dbPort;
  }

  public void setDbPort(String dbPort)
  {
	this.dbPort = dbPort;
  }

  public String getDbName()
  {
	return dbName;
  }

  public void setDbName(String dbName)
  {
	this.dbName = dbName;
  }

  public String getDbPwd()
  {
	return dbPwd;
  }

  public void setDbPwd(String dbPwd)
  {
	this.dbPwd = dbPwd;
  }

  public Properties getLoadFromProperties()
  {
	return null;
  }

  public void setLoadFromProperties(final Properties props)
  {
	try
	{
	  final Group dbConnectionGroup = props.getGroup("dbConnection");
	  if (dbConnectionGroup != null)
	  {
		final String dbDriver = dbConnectionGroup.getEntry("dbDriver").getValue();
		final String dbAddress = dbConnectionGroup.getEntry("dbAddress").getValue();
		this.dbPwd = dbConnectionGroup.getEntry("dbPasswdFile").getValue();
		if ("oracle.jdbc.driver.OracleDriver".equals(dbDriver))
		{
		  this.dbFamily = "oracle";
		  final String connStr = dbAddress.substring("jdbc:oracle:thin:@".length());
		  final String[] addrComps = connStr.split(":");
		  this.dbHost = addrComps[0];
		  this.dbPort = addrComps[1];
		  this.dbName = addrComps[2];
		}
		else
		{
		  this.dbFamily = "mssql";
		  final String connStr = dbAddress.substring("jdbc:JSQLConnect://".length());
		  final String[] addrComps = connStr.split(":");
		  this.dbHost = addrComps[0];
		  this.dbPort = addrComps[1];
		}
	  }
	}
	catch (final NoSuchElementException e)
	{
	  System.out.println("Could not find config element");
	}
  }
}
