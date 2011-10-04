/**
 * 
 */
package com.baxter.config.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ykryshchuk
 * 
 */
@XmlRootElement(name = "entry", namespace = Properties.NS)
public class Entry extends AbstractValue
{

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

  @Override
  public String toString()
  {
	return "Entry(" + super.toString() + "){" + getAlias() + "}";
  }

}
