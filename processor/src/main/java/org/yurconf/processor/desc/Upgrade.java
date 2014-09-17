/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package org.yurconf.processor.desc;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 * TODO update XSD
 * 
 * @author ykryshchuk
 * @sice ${developmentVersion}
 */
public class Upgrade
{
  @XmlAttribute(name = "version")
  private String fromVersion;

  @XmlAttribute(name = "toVersion")
  private String toVersion;

  @XmlElements({ @XmlElement(name = "add", type = UpgradeAddFile.class),
	  @XmlElement(name = "remove", type = UpgradeRemoveFile.class),
	  @XmlElement(name = "move", type = UpgradeMoveFile.class),
	  @XmlElement(name = "transform", type = UpgradeTransform.class) })
  private List<? extends AbstractUpgradeFile> commands;

  public String getFromVersion()
  {
	return fromVersion;
  }

  public String getToVersion()
  {
	return toVersion;
  }

  public List<? extends AbstractUpgradeFile> getCommands()
  {
	return this.commands;
  }
  
  @Override
  public String toString()
  {
	final StringBuilder str = new StringBuilder("Upgrade[");
	str.append(getFromVersion());
	str.append("->");
	str.append(getToVersion());
	str.append("]");
    return str.toString();
  }

}
