/**
 * 
 */
package com.baxter.config.model.log4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

  private static final Comparator<Appender> APPENDER_COMPARATOR = new Comparator<Appender>()
  {
	@Override
	public int compare(Appender o1, Appender o2)
	{
	  return o1.getName().compareTo(o2.getName());
	}
  };

  private static final Comparator<AbstractLogger> LOGGER_COMPARATOR = new Comparator<AbstractLogger>()
  {
	@Override
	public int compare(AbstractLogger o1, AbstractLogger o2)
	{
	  return o1.getName().compareTo(o2.getName());
	}
  };

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
	Collections.sort(taps, APPENDER_COMPARATOR);
	return taps;
  }

  public List<AbstractLogger> getAllLoggers()
  {
	final List<AbstractLogger> all = new ArrayList<AbstractLogger>();
	all.addAll(loggers);
	all.addAll(categories);
	Collections.sort(all, LOGGER_COMPARATOR);
	if ( root != null ) {
	  all.add(0, this.root);
	}
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
