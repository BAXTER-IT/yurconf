/**
 * 
 */
package com.baxter.config.bean;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.baxter.config.model.log4j.Configuration;
import com.baxter.config.model.properties.Properties;
import com.baxter.config.servlet.Component;
import com.baxter.config.servlet.ConfigurationType;
import com.baxter.config.servlet.StoreManager;

/**
 * @author ykryshchuk
 * 
 */
public class ConfigPersister
{

  private static final String PROPS_BEAN_NAME = "props";

  private static final String LOGS_BEAN_NAME = "logs";

  private StoreManager storeManager;

  private String tag;

  private String storedTag;

  public String getTag()
  {
	return tag;
  }

  public void setTag(String tag)
  {
	this.tag = tag;
  }

  public String getStoredTag()
  {
	return storedTag;
  }

  public void setStoredTag(String storedTag)
  {
	this.storedTag = storedTag;
  }

  public List<String> getStoredTags()
  {
	final List<String> tags = new ArrayList<String>(storeManager.getStoredTags());
	tags.remove("default");
	return tags;
  }

  public StoreManager getStoreManager()
  {
	return storeManager;
  }

  public void setStoreManager(StoreManager storeManager)
  {
	this.storeManager = storeManager;
  }

  public void save(final HttpSession session, final Messages msg)
  {
	try
	{
	  {
		final Properties props = (Properties) session.getAttribute(PROPS_BEAN_NAME);
		final Marshaller mProps = createMarshaller(Properties.class);
		final OutputStream streamProps = storeManager.getOutputStream("properties.xml", false);
		try
		{
		  mProps.marshal(props, streamProps);
		}
		finally
		{
		  streamProps.close();
		}
	  }
	  {
		final LogsProvider logs = (LogsProvider) session.getAttribute(LOGS_BEAN_NAME);
		final Marshaller mLogs = createMarshaller(Configuration.class);
		for (Component comp : Component.values())
		{
		  final OutputStream streamLogs = storeManager.getOutputStream(comp.getFileName(ConfigurationType.log4j), false);
		  try
		  {
			mLogs.marshal(logs.get(comp), streamLogs);
		  }
		  finally
		  {
			streamLogs.close();
		  }
		}
	  }
	  if (!tag.isEmpty() && !"default".equals(tag))
	  {
		this.storeManager.tag(tag, msg);
	  }
	}
	catch (final Exception e)
	{
	  msg.add(e);
	}
  }

  public void load(final HttpSession session, final Messages msg)
  {
	storeManager.untag(storedTag, msg);
	try
	{
	  final JAXBContext jaxb = JAXBContext.newInstance(Properties.class, Configuration.class);
	  final Unmarshaller um = jaxb.createUnmarshaller();

	  {
		final InputStream streamProps = this.storeManager.getInputStream("properties.xml");
		try
		{
		  final Properties props = Properties.class.cast(um.unmarshal(streamProps));
		  session.setAttribute(PROPS_BEAN_NAME, props);
		}
		finally
		{
		  streamProps.close();
		}
	  }
	  {
		final LogsProvider existingLogs = (LogsProvider) session.getAttribute(LOGS_BEAN_NAME);
		final LogsProvider logs;
		if (existingLogs == null)
		{
		  logs = new LogsProvider();
		  session.setAttribute(LOGS_BEAN_NAME, logs);
		}
		else
		{
		  logs = existingLogs;
		}
		for (Component comp : Component.values())
		{
		  final InputStream streamLogs = this.storeManager.getInputStream(comp.getFileName(ConfigurationType.log4j));
		  try
		  {
			final Configuration config = Configuration.class.cast(um.unmarshal(streamLogs));
			logs.put(comp, config);
		  }
		  finally
		  {
			streamLogs.close();
		  }
		}
	  }
	}
	catch (final Exception e)
	{
	  if (msg != null)
	  {
		msg.add(e);
	  }
	  else
	  {
		e.printStackTrace();
	  }
	}
  }

  private Marshaller createMarshaller(final Class<?> type) throws JAXBException
  {
	final JAXBContext jaxb = JAXBContext.newInstance(type);
	final Marshaller m = jaxb.createMarshaller();
	m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	return m;
  }

  public HttpSession getLoadFromSession()
  {
	return null;
  }

  public void setLoadFromSession(final HttpSession session)
  {
	if (session.getAttribute(PROPS_BEAN_NAME) == null || session.getAttribute(LOGS_BEAN_NAME) == null)
	{
	  load(session, null);
	}
  }

}
