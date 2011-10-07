/**
 * 
 */
package com.baxter.config.model.log4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ykryshchuk
 * 
 */
@XmlRootElement(name = "configuration", namespace = Configuration.NS)
public class Configuration implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

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

  public List<Appender> getTargetAppenders()
  {
	final List<Appender> taps = new ArrayList<Appender>();
	for (Appender app : this.appenders)
	{
	  if (app.getAppenderRefs().isEmpty())
	  {
		taps.add(app);
	  }
	}
	return taps;
  }
  
  public List<Logger> getAllLoggers() {
	final List<Logger> all = new ArrayList<Logger>();
	all.addAll(loggers);
	all.addAll(categories);
	return all;
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
