/**
 * 
 */
package com.baxter.config.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ykryshchuk
 * 
 */
@XmlRootElement(name = "pretag", namespace = Properties.NS)
public class Alias extends AbstractValue
{
  public Alias()
  {
  }

  Alias(final String key, final String value)
  {
	super(key, value);
  }

  @Override
  public String toString()
  {
	return "Alias{" + super.toString() + "}";
  }

}
