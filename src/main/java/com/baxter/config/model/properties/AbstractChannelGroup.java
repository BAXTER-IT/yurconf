/**
 * 
 */
package com.baxter.config.model.properties;


/**
 * @author ykryshchuk
 *
 */
public abstract class AbstractChannelGroup extends Group 
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

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
  
  public abstract void setChannelName( final String name );
  
  public void setJMS( final int index ) {
	addEntryWithAlias("Host", "host"+index);
	addEntryWithAlias("Port", "port"+index);
	addEntryWithAlias("UserName", "userName"+index);
	addEntryWithAlias("Password", "password"+index);
  }
  
}
