/**
 * 
 */
package com.baxter.config.processor.desc;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public abstract class AbstractUpgradeFileCommand extends AbstractUpgradeCommand
{
  @XmlAttribute(name = "file")
  private String file;

  public String getFile()
  {
	return this.file;
  }

}
