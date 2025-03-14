/**
 * 
 */
package com.baxter.config.processor.desc;

import jakarta.xml.bind.annotation.XmlAttribute;

/**
 * @author ykryshchuk
 * @since ${developmentVersion}
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
