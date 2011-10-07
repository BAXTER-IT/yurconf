package com.baxter.config.model.log4j;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

public class Layout extends AbstractParametrized implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @XmlTransient
  public String getPattern()
  {
	return getParam("ConversionPattern").getValue();
  }
  
  public void setPattern( final String pattern ) {
	getParam("ConversionPattern").setValue(pattern);
  }

}
