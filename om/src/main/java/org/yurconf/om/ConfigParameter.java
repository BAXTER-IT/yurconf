/*
 * Yurconf Object Model
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.om;

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
