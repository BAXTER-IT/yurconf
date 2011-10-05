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
@XmlRootElement(name = "properties", namespace = Properties.NS)
public class Properties extends AbstractContainer
{
  static final String NS = "http://baxter.com/config";

  @XmlAttribute(name = "version")
  private String version = "1.1";

  public String getVersion()
  {
	return version;
  }

  public int getJMSInstancesCount()
  {
	int count = 0;
	try
	{
	  while (true)
	  {
		final int index = count + 1;
		getAlias("Host" + index);
		getAlias("Port" + index);
		getAlias("Router" + index);
		getAlias("UserName" + index);
		getAlias("Password" + index);
		count++;
	  }
	}
	catch (final NoSuchElementException e)
	{
	  // no more aliases found
	  return count;
	}
  }
  
  public void addNewJmsInstance( final String host, final String port, final String router, final String username, final String password ) {
	final int newIdx = getJMSInstancesCount() + 1;
	addAlias("Host"+newIdx, host);
	addAlias("Port"+newIdx, port);
	addAlias("Router"+newIdx, router);
	addAlias("UserName"+newIdx, username);
	addAlias("Password"+newIdx, password);
  }

  public String getLoadFrom()
  {
	return null;
  }

  public void setLoadFrom(final String where)
  {
	System.out.println("Loading from " + where);
  }

}
