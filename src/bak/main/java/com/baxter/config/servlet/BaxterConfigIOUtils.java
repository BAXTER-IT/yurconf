package com.baxter.config.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

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
	case log4j:
	  copyLog4jXml(inputStream, outputStream);
	  break;
	case properties:
	  if (!comp.isServer())
	  {
		copyClientProperies(inputStream, outputStream, comp);
		break;
	  }
	default:
	  IOUtils.copy(inputStream, outputStream);
	}
  }

  private static void copyLog4jXml(final InputStream inputStream, final OutputStream outputStream) throws IOException
  {
	final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	final OutputStreamWriter writer = new OutputStreamWriter(outputStream);
	String line;
	boolean doctypeAdded = false;
	while ((line = reader.readLine()) != null)
	{
	  if (!doctypeAdded && line.indexOf("<?xml ") != -1)
	  {
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">\n");
		doctypeAdded = true;
	  } else {
	  if (line.contains("ns2"))
	  {
		writer.write(line.replace("ns2", "log4j"));
	  }
	  else
	  {
		writer.write(line);
	  }
	  writer.write("\n");
	  }
	}
	writer.flush();
  }

  private static void copyClientProperies(final InputStream inputStream, final OutputStream outputStream, final Component comp)
	  throws JAXBException, IOException
  {
	final JAXBContext jaxb = JAXBContext.newInstance(Properties.class);
	final Unmarshaller umProps = jaxb.createUnmarshaller();
	final Marshaller mProps = jaxb.createMarshaller();
	mProps.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	mProps.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//	mProps.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	final Properties clientProperties = Properties.class.cast(umProps.unmarshal(inputStream));
	final Import importElem = new Import("../config/properties-" + comp + "-w.xml", true);
	clientProperties.addImportElem(importElem);
	mProps.marshal(clientProperties, outputStream);
  }
}
