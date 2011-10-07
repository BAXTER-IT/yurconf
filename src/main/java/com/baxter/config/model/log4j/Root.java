package com.baxter.config.model.log4j;

import java.io.Serializable;

public class Root extends AbstractLogger implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public String getName()
  {
	return "root";
  }
  
  @Override
  public boolean isAdditivity()
  {
    return false;
  }
  
  @Override
  public void setAdditivity(boolean value)
  {
    // do nothing
  }
  
  @Override
  public boolean isAdditivityIgnored()
  {
    return true;
  }

}
