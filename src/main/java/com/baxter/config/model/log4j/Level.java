package com.baxter.config.model.log4j;

import javax.xml.bind.annotation.XmlAttribute;

public class Level
{
  @XmlAttribute(name = "value")
  private String value;

  public String getValue()
  {
    return value;
  }
  
}
