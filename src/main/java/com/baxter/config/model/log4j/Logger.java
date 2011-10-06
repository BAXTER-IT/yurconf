package com.baxter.config.model.log4j;

import javax.xml.bind.annotation.XmlAttribute;

public class Logger extends AbstractLogger
{

  @XmlAttribute(name = "name")
  private String name;

  @XmlAttribute(name = "additivity")
  private boolean additivity = true;

  public String getName()
  {
	return name;
  }
  
  public boolean isAdditivity()
  {
	return additivity;
  }

}
