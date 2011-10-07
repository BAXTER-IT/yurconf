package com.baxter.config.model.log4j;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

public class Logger extends AbstractLogger implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;



  @XmlAttribute(name = "name")
  private String name;

  @XmlAttribute(name = "additivity")
  private boolean additivity = true;

  @Override
  public String getName()
  {
	return name;
  }
  
  public boolean isAdditivity()
  {
	return additivity;
  }
  
  @Override
  public void setAdditivity(boolean value)
  {
    this.additivity = value;
  }
  
  @Override
  public boolean isAdditivityIgnored()
  {
    return false;
  }

}
