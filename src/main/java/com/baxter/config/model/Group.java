/**
 * 
 */
package com.baxter.config.model;

import java.util.NoSuchElementException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ykryshchuk
 * 
 */
@XmlRootElement(name = "group", namespace=Properties.NS)
public class Group extends AbstractContainer
{

  @XmlAttribute(name = "key")
  private String key;

  public Group()
  {
  }

  public Group(final String key)
  {
	this.key = key;
  }

  public String getKey()
  {
	return key;
  }
  
  public String getChannelName() {
	  try {
		return getEntry("TName").getValue();
	  } catch ( final NoSuchElementException e ) {
		return getEntry("QName").getValue();
	  }
  }
  
  public int getJMSIndex() {
	final Entry hostEntry = getEntry("Host");
	final String hostAlias = hostEntry.getAlias();
	return Integer.parseInt(hostAlias.substring(4));
  }
  
  public String getChannelType() {
	try {
	  getEntry("Host");
	  getEntry("Port");
	  getEntry("UserName");
	  getEntry("Password");
	  try {
		getEntry("TName");
		return "Topic";
	  } catch ( final NoSuchElementException e ) {
		getEntry("QName");
		getEntry("QRouter");
		return "Queue";
	  }
	} catch ( final NoSuchElementException e ) {
	  return null;
	}
  }
  
  public AbstractChannelGroup toChannel() {
	final AbstractChannelGroup channel;
	if ( "Topic".equals(getChannelType()) ) {
	  channel = new TopicGroup(getKey());
	} else if ("Queue".equals(getChannelType())) {
	  channel = new QueueGroup(getKey());
	} else {
	  return null;
	}
	channel.copyFrom(this);
	return channel;
  }

}
