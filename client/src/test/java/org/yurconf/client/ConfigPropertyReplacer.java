/*
 * Baxter Configuration Client
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
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
