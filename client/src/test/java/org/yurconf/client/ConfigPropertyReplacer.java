/*
 * Yurconf Client
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.client;

import org.yurconf.client.DefaultEnvironment;

/**
 * Replacer for the config property with restore functionality.
 *
 * @author ykryshchuk
 * @since 1.5
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
