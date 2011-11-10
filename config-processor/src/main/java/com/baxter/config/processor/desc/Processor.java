/**
 * 
 */
package com.baxter.config.processor.desc;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * The processor entry descriptor.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class Processor
{

  @XmlAttribute(name = "class")
  private String className;

  public String getClassName()
  {
	return className;
  }

}
