/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
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
