package com.baxter.config.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;

import com.baxter.config.model.properties.Import;
import com.baxter.config.model.properties.Properties;

public class BaxterConfigIOUtils
{

  public static void copy(final InputStream inputStream, final OutputStream outputStream, final ConfigurationType type,
	  final Component comp) throws JAXBException, IOException
  {
	switch (type)
	{
	case properties:
	  if (!comp.isServer())
	  {
		copyClientProperies(inputStream, outputStream, comp);
		break;
	  }
	case log4j:
	default:
	  IOUtils.copy(inputStream, outputStream);
	}
  }

  private static void copyClientProperies(final InputStream inputStream, final OutputStream outputStream, final Component comp)
	  throws JAXBException, IOException
  {
	final JAXBContext jaxb = JAXBContext.newInstance(Properties.class);
	final Unmarshaller umProps = jaxb.createUnmarshaller();
	final Marshaller mProps = jaxb.createMarshaller();
	final Properties clientProperties = Properties.class.cast(umProps.unmarshal(inputStream));
	final Import importElem = new Import("./properties-" + comp + "-w.xml", true);
	clientProperties.addImportElem(importElem);
	mProps.marshal(clientProperties, outputStream);
  }
}
