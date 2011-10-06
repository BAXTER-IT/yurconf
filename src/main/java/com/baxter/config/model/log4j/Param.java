package com.baxter.config.model.log4j;

import javax.xml.bind.annotation.XmlAttribute;

public class Param
{

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
  
}
