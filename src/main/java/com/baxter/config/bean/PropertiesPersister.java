/**
 * 
 */
package com.baxter.config.bean;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.baxter.config.model.Properties;
import com.baxter.config.servlet.StoreManager;

/**
 * @author ykryshchuk
 * 
 */
public class PropertiesPersister
{

  private static final String PROPS_BEAN_NAME = "props";
  
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
	return storeManager.getStoredTags();
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
	final Properties props = (Properties) session.getAttribute(PROPS_BEAN_NAME);
	try
	{
	  final Marshaller mProps = createMarshaller(Properties.class);
	  final StringWriter writerProps = new StringWriter();
	  try
	  {
		mProps.marshal(props, writerProps);
		writerProps.flush();
		System.out.println("Props: \n" + writerProps.toString());
	  }
	  finally
	  {
		writerProps.close();
	  }
	}
	catch (final Exception e)
	{
	  msg.add(e);
	}
  }

  public void load(final HttpSession session, final Messages msg)
  {
	final Properties props = null;
	session.setAttribute(PROPS_BEAN_NAME, props);
  }

  private Marshaller createMarshaller(final Class<?> type) throws JAXBException
  {
	final JAXBContext jaxb = JAXBContext.newInstance(type);
	final Marshaller m = jaxb.createMarshaller();
	m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	return m;
  }

}
