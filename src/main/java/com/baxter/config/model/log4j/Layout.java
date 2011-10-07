package com.baxter.config.model.log4j;

import java.io.Serializable;

public class Layout extends AbstractParametrized implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public String getPattern()
  {
	return getParam("ConversionPattern").getValue();
  }
  
  public void setPattern( final String pattern ) {
	getParam("ConversionPattern").setValue(pattern);
  }

}
