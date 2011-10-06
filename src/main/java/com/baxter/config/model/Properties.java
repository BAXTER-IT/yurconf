/**
 * 
 */
package com.baxter.config.model;

import java.util.NoSuchElementException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.baxter.config.bean.NewChannelBean;
import com.baxter.config.bean.NewJmsBean;
import com.baxter.config.bean.PropertiesPersister;

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

  public void deleteLastJMS()
  {
	final int index = getJMSInstancesCount();
	if (index != 0)
	{
	  removeAlias("Host" + index);
	  removeAlias("Port" + index);
	  removeAlias("Router" + index);
	  removeAlias("UserName" + index);
	  removeAlias("Password" + index);
	}
  }

  public void addNewJmsInstance(final NewJmsBean newJms)
  {
	final int newIdx = getJMSInstancesCount() + 1;
	addAlias("Host" + newIdx, newJms.getHost());
	addAlias("Port" + newIdx, String.valueOf(newJms.getPort()));
	addAlias("Router" + newIdx, newJms.getRouter());
	addAlias("UserName" + newIdx, newJms.getUsername());
	addAlias("Password" + newIdx, newJms.getPassword());
  }

  public void addNewChannel(final NewChannelBean newChannel)
  {
	final AbstractChannelGroup channel;
	if ("T".equals(newChannel.getType()))
	{
	  channel = addTopic(newChannel.getAlias());
	}
	else
	{
	  channel = addQueue(newChannel.getAlias());
	}
	channel.setJMS(newChannel.getJmsIndex());
	channel.setChannelName(newChannel.getName());
  }

}
