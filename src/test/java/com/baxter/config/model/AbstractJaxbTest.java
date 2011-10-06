package com.baxter.config.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.baxter.config.model.log4j.Configuration;
import com.baxter.config.model.properties.Properties;

public abstract class AbstractJaxbTest
{

  private JAXBContext createJaxb(final Object source) throws JAXBException
  {
	if (Properties.class.isInstance(source))
	{
	  return JAXBContext.newInstance(Properties.class);
	}
	else if (Configuration.class.isInstance(source))
	{
	  return JAXBContext.newInstance(Configuration.class);
	}
	else
	{
	  return JAXBContext.newInstance(Properties.class, Configuration.class);
	}
  }

  protected Object deserializeFromStream(final InputStream stream) throws JAXBException
  {
	final Unmarshaller um = createJaxb(null).createUnmarshaller();
	return um.unmarshal(stream);
  }

  protected String serializeToString(final Object o) throws JAXBException, IOException
  {
	final Marshaller m = createJaxb(o).createMarshaller();
	m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	final StringWriter writer = new StringWriter();
	try
	{
	  m.marshal(o, writer);
	  writer.flush();
	  return writer.toString();
	}
	finally
	{
	  writer.close();
	}
  }

}
