/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.processor.desc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * The processor entry descriptor.
 *
 * @author ykryshchuk
 * @since 1.5
 */
public class Processor
{

  @XmlAttribute(name = "class")
  private String className;

  @XmlAttribute(name = "type")
  private String configurationType;

  @XmlElement(name = "parameter")
  private final List<Parameter> parameters = new ArrayList<Parameter>();

  public String getClassName()
  {
	return className;
  }

  public List<Parameter> getParameters()
  {
	return parameters;
  }

  void setClassName(final String className)
  {
	this.className = className;
  }

  public String getConfigurationType()
  {
	return configurationType;
  }

  void setConfigurationType(final String configurationType)
  {
	this.configurationType = configurationType;
  }

  @Override
  public String toString()
  {
	final StringBuilder str = new StringBuilder("Processor [");
	str.append(getClassName());
	str.append(":");
	str.append(getConfigurationType());
	str.append("]");
	return str.toString();
  }

}
