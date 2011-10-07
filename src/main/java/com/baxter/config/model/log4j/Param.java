package com.baxter.config.model.log4j;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

public class Param implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;



  @XmlAttribute(name="name")
  private String name;
  
  @XmlAttribute(name="value")
  private String value;

  public String getName()
  {
    return name;
  }

  public String getValue()
  {
    return value;
  }
  
  void setValue( final String value ) {
	this.value = value;
  }
  
}
