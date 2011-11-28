/**
 * 
 */
package com.baxter.config.processor.desc;

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
