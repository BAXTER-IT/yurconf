/**
 * 
 */
package com.baxter.config.model.properties;

import java.io.Serializable;

/**
 * @author ykryshchuk
 * 
 */
public class Alias extends AbstractValue implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

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
