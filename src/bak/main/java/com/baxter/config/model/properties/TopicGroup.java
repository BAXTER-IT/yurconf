/**
 * 
 */
package com.baxter.config.model.properties;

import java.io.Serializable;

/**
 * @author ykryshchuk
 * 
 */
public class TopicGroup extends AbstractChannelGroup implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;



  public TopicGroup() {
	
  }
  
  TopicGroup( final String key ) {
	super(key);
  }

  public Entry getTNameEntry()
  {
	return getEntry("TName");
  }
  
  @Override
  public void setChannelName(String name)
  {
    addEntryWithValue("TName", name);
  }

}
