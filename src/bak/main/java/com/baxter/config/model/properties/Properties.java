/**
 * 
 */
package com.baxter.config.model.properties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.baxter.config.bean.DBConnectionBean;
import com.baxter.config.bean.Messages;
import com.baxter.config.bean.NewChannelBean;
import com.baxter.config.bean.NewJmsBean;

/**
 * @author ykryshchuk
 * 
 */
@XmlRootElement(name = "properties", namespace = Properties.NS)
public class Properties extends AbstractContainer implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  static final String NS = "http://baxter.com/config";

  @XmlAttribute(name = "version")
  private String version = "1.1";

  @XmlElement(name = "import", namespace = Properties.NS)
  private List<Import> importElemList;

  public String getVersion()
  {
	return version;
  }

  public void removeImportElem(Import elem)
  {
	if (importElemList != null)
	{
	  importElemList.remove(elem);
	}
  }

  public void addImportElem(Import elem)
  {
	if (importElemList == null)
	{
	  importElemList = new ArrayList<Import>();
	}
	importElemList.add(elem);
  }

  public int getJMSInstancesCount()
  {
	int count = 0;
	try
	{
	  while (true)
	  {
		final int index = count + 1;
		getAlias("host" + index);
		getAlias("port" + index);
		getAlias("router" + index);
		getAlias("userName" + index);
		getAlias("password" + index);
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
	  removeAlias("host" + index);
	  removeAlias("port" + index);
	  removeAlias("router" + index);
	  removeAlias("userName" + index);
	  removeAlias("password" + index);
	}
  }

  public void addNewJmsInstance(final NewJmsBean newJms)
  {
	final int newIdx = getJMSInstancesCount() + 1;
	addAlias("host" + newIdx, newJms.getHost());
	addAlias("port" + newIdx, String.valueOf(newJms.getPort()));
	addAlias("router" + newIdx, newJms.getRouter());
	addAlias("userName" + newIdx, newJms.getUsername());
	addAlias("password" + newIdx, newJms.getPassword());
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

  public void arrangeChannel(final Group channel, final int jmsIndex)
  {
	final AbstractChannelGroup channelGroup;
	if (AbstractChannelGroup.class.isInstance(channel))
	{
	  channelGroup = AbstractChannelGroup.class.cast(channel);
	}
	else
	{
	  channelGroup = channel.toChannel();
	}
	channelGroup.setJMS(jmsIndex);
  }

  public void changeDBConnection(final DBConnectionBean dbConnection, final Messages msg)
  {
	final Group dbConnectionGroup = getGroup("dbConnection");
	if ("oracle".equals(dbConnection.getDbFamily()))
	{
	  dbConnectionGroup.addEntryWithValue("dbDriver", "oracle.jdbc.driver.OracleDriver");
	  dbConnectionGroup.addEntryWithValue("dbAddress",
		  "jdbc:oracle:thin:@" + dbConnection.getDbHost() + ":" + dbConnection.getDbPort() + ":" + dbConnection.getDbName());
	}
	else
	{
	  dbConnectionGroup.addEntryWithValue("dbDriver", "com.jnetdirect.jsql.JSQLDriver");
	  dbConnectionGroup.addEntryWithValue("dbAddress",
		  "jdbc:JSQLConnect://" + dbConnection.getDbHost() + ":" + dbConnection.getDbPort());
	}
	dbConnectionGroup.addEntryWithValue("dbPasswdFile", dbConnection.getDbPwd());
  }

}
