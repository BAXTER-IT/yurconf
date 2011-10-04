/**
 * 
 */
package com.baxter.config.model;

/**
 * @author ykryshchuk
 * 
 */
public class QueueGroup extends AbstractChannelGroup
{
  
  public QueueGroup() {
	
  }
  
  QueueGroup( final String key ) {
	super(key);
  }

  public Entry getQRouterEntry()
  {
	return getEntry("QRouter");
  }

  public Entry getQNameEntry()
  {
	return getEntry("QName");
  }

}
