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
