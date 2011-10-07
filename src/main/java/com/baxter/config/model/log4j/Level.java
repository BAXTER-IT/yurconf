package com.baxter.config.model.log4j;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

public class Level implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  @XmlAttribute(name = "value")
  private String value;

  public String getValue()
  {
    return value;
  }
  
}
