/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package org.yurconf.processor.desc;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author ykryshchuk
 * @since 1.5
 */
public class UpgradeMoveFile extends AbstractUpgradeFile
{

  @XmlAttribute(name = "to")
  private String to;

  public String getTo()
  {
	return this.to;
  }

}
