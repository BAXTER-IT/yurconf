/**
 * 
 */
package com.baxter.config.model;

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

}
