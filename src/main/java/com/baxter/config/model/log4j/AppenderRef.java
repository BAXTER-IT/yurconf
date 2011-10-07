package com.baxter.config.model.log4j;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

public class AppenderRef implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @XmlAttribute(name = "ref")
  private String ref;

  public String getRef()
  {
	return ref;
  }
}
