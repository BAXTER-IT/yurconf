package com.baxter.config.model.log4j;

import java.io.Serializable;
import java.util.NoSuchElementException;

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
	try
	{
	  return getParam("ConversionPattern").getValue();
	}
	catch (final NoSuchElementException e)
	{
	  System.out.println("Failed to get conversion patterm param");
	  return null;
	}
  }

  public void setPattern(final String pattern)
  {
	getParam("ConversionPattern").setValue(pattern);
  }

}
