/**
 * 
 */
package com.baxter.config.om;

/**
 * Configuration parameter.
 * 
 * @author xpdev
 * @since ${developmentVersion}
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

}
