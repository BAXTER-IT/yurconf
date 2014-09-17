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
 * @since ${developmentVersion}
 */
public abstract class AbstractUpgradeFile implements FilenameProvider
{
  @XmlAttribute(name = "file")
  private String file;

  @XmlAttribute(name = "regexp")
  private String regexp;

  @Override
  public String getFileNameMask()
  {
	return this.file;
  }

  @Override
  public String getFileNamePattern()
  {
	return this.regexp;
  }

}
