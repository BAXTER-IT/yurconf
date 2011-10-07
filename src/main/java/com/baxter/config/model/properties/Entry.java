/**
 * 
 */
package com.baxter.config.model.properties;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author ykryshchuk
 * 
 */
public class Entry extends AbstractValue implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;



  @XmlAttribute(name = "pretag")
  private String alias;

  static Entry forValue(final String key, final String value)
  {
	return new Entry(key, null, value);
  }

  static Entry forAlias(final String key, final String alias)
  {
	return new Entry(key, alias, null);
  }

  public Entry()
  {
  }

  private Entry(final String key, final String alias, final String value)
  {
	super(key, value);
	this.alias = alias;
  }

  public String getAlias()
  {
	return alias;
  }
  
  void setAlias( final String alias ) {
	this.alias = alias;
  }

  @Override
  public String toString()
  {
	return "Entry(" + super.toString() + "){" + getAlias() + "}";
  }

}
