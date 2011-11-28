/**
 * 
 */
package com.baxter.config.processor.desc;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author ykryshchuk
 * @since ${developmentVersion}
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
