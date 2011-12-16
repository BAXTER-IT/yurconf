package com.baxter.config.client;

/**
 * Replacer for the config property with restore functionality.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
class ConfigPropertyReplacer
{
  private final String propertyName;
  private final String originalValue;

  ConfigPropertyReplacer(final String propertyName, final String newValue)
  {
	this.propertyName = DefaultEnvironment.PREFIX + propertyName;
	this.originalValue = System.getProperty(this.propertyName);
	System.setProperty(this.propertyName, newValue);
  }

  void restore()
  {
	if (this.originalValue == null)
	{
	  System.clearProperty(this.propertyName);
	}
	else
	{
	  System.setProperty(this.propertyName, this.originalValue);
	}
  }
}