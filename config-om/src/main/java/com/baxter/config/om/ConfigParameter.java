/*
 * Baxter Configuration Object Model
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.om;

/**
 * Configuration parameter.
 *
 * @author xpdev
 * @since 1.5
 */
public class ConfigParameter
{

  private final String name;

  private final String value;

  public ConfigParameter(final String name, final String value)
  {
	this.name = name;
	this.value = value;
  }

  public String getName()
  {
	return name;
  }

  public String getValue()
  {
	return value;
  }

  @Override
  public String toString()
  {
	final StringBuilder str = new StringBuilder(getName());
	str.append("=");
	str.append(getValue());
    return str.toString();
  }

}
