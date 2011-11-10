/**
 * 
 */
package com.baxter.config.processor.desc;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * Processor parameter.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
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

}
