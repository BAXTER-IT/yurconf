/**
 * 
 */
package com.baxter.config.model.properties;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author ykryshchuk
 * 
 */
public abstract class AbstractValue
{

  @XmlAttribute(name = "key")
  private String key;

  @XmlValue
  private String value;

  protected AbstractValue()
  {

  }

  protected AbstractValue(final String key, final String value)
  {
	this.key = key;
	this.value = value;
  }

  public String getKey()
  {
	return key;
  }

  public String getValue()
  {
	return value;
  }
  
  void setValue( final String value ) {
	this.value = value;
  }
  
  @Override
  public String toString()
  {
    return getKey() + ";" + getValue();
  }

}
