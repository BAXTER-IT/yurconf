/**
 * 
 */
package com.baxter.config.model.properties;

/**
 * @author ykryshchuk
 * 
 */
public class TopicGroup extends AbstractChannelGroup
{

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
