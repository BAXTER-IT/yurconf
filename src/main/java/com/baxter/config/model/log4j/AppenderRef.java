package com.baxter.config.model.log4j;

import javax.xml.bind.annotation.XmlAttribute;

public class AppenderRef
{
  @XmlAttribute(name = "ref")
  private String ref;

  public String getRef()
  {
	return ref;
  }
}
