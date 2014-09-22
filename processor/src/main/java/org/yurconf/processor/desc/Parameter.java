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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * Processor parameter.
 *
 * @author ykryshchuk
 * @since 1.5
 */
public class Parameter
{

  @XmlAttribute(name = "name")
  private String name;

  @XmlValue
  private String value;

  /**
   * Parameterles constructor.
   */
  public Parameter()
  {

  }

  Parameter(final String name, final String value)
  {
	this.name = name;
	this.value = value;
  }

  public String getName()
  {
	return name;
  }

  void setName(final String name)
  {
	this.name = name;
  }

  public String getValue()
  {
	return value;
  }

  void setValue(final String value)
  {
	this.value = value;
  }

  @Override
  public String toString()
  {
	final StringBuilder str = new StringBuilder("Parameter [");
	str.append(getName());
	str.append("=");
	str.append(getValue());
	str.append("]");
	return str.toString();
  }

}
