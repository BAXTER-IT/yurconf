/**
 * 
 */
package com.baxter.config.model;

/**
 * @author ykryshchuk
 *
 */
public abstract class AbstractChannelGroup extends Group 
{

  protected AbstractChannelGroup()
  {
	
  }
  
  protected AbstractChannelGroup( final String key ) {
	super(key);
  }

  public Entry getHostEntry() {
	return getEntry("Host");
  }
  
  public Entry getPortEntry() {
	return getEntry("Port");
  }
  
  public Entry getHost2Entry() {
	return getEntry("Host2");
  }
  
  public Entry getPort2Entry() {
	return getEntry("Port2");
  }
  
  public Entry getUserNameEntry() {
	return getEntry("UserName");
  }
  
  public Entry getPasswordEntry() {
	return getEntry("Password");
  }
  
}
