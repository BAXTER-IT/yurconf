/**
 * 
 */
package com.baxter.config.model.log4j;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ykryshchuk
 * 
 */
@XmlRootElement(name = "configuration", namespace = Configuration.NS)
public class Configuration
{
  static final String NS = "http://jakarta.apache.org/log4j/";

  @XmlElement(name = "appender")
  private List<Appender> appenders = new ArrayList<Appender>();

  @XmlElement(name = "logger")
  private List<Logger> loggers = new ArrayList<Logger>();

  @XmlElement(name = "category")
  private List<Category> categories = new ArrayList<Category>();

  @XmlElement(name = "root")
  private Root root;

  public List<Appender> getAppenders()
  {
	return appenders;
  }

  public List<Logger> getLoggers()
  {
	return loggers;
  }

  public List<Category> getCategories()
  {
	return categories;
  }

  public Root getRoot()
  {
	return root;
  }

}
