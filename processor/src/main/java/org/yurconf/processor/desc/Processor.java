/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
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
