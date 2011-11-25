/**
 * 
 */
package com.baxter.config.processor.desc;

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

  @XmlElements({ @XmlElement(name = "add", type = UpgradeAddFileCommand.class),
	  @XmlElement(name = "remove", type = UpgradeRemoveFileCommand.class),
	  @XmlElement(name = "move", type = UpgradeMoveFileCommand.class),
	  @XmlElement(name = "transform", type = UpgradeTransformCommand.class) })
  private List<? extends AbstractUpgradeCommand> commands;

  public String getFromVersion()
  {
	return fromVersion;
  }

  public String getToVersion()
  {
	return toVersion;
  }

  public List<? extends AbstractUpgradeCommand> getCommands()
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
