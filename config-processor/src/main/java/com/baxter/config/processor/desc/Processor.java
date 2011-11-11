/**
 * 
 */
package com.baxter.config.processor.desc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

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
  
  @XmlAttribute(name = "type")
  private String configurationType;

  @XmlElement(name = "parameter")
  private List<Parameter> parameters = new ArrayList<Parameter>();

  public String getClassName()
  {
	return className;
  }

  public List<Parameter> getParameters()
  {
	return parameters;
  }

  void setClassName(final String className)
  {
	this.className = className;
  }

  public String getConfigurationType()
  {
    return configurationType;
  }

  void setConfigurationType(final String configurationType)
  {
    this.configurationType = configurationType;
  }

}
