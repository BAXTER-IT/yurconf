/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.processor.desc;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author ykryshchuk
 * @since 1.5
 */
public class UpgradeTransform extends AbstractUpgradeFile
{
  @XmlAttribute(name = "stylesheet")
  private String stylesheet;

  public String getStylesheet()
  {
	return stylesheet;
  }
}
