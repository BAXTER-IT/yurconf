package com.baxter.config.model.log4j;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

public class Level implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private static final String[] LEVELS = { "", "TRACE", "DEBUG", "INFO", "WARN", "ERROR" };


  @XmlAttribute(name = "value")
  private String value;
  
  public static List<String> getValues() {
	return Arrays.asList(LEVELS);
  }
  
  public Level() {
	
  }
  
  Level( final String value ) {
	this.value = value;
  }

  public String getValue()
  {
    return value;
  }

  void setValue(String value)
  {
    this.value = value;
  }
  
}
