/**
 * 
 */
package com.baxter.config.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * @author ykryshchuk
 * 
 */
public class PropertiesTest
{

  @Test
  public void testSerialization() throws Exception
  {
	final Properties p = new Properties();
	p.addAlias("xmlUserId", "m");
	p.addAlias("peHome", "/opt/baxter/pe");
	p.addAlias("dummyAlias", null);
	p.addAlias("True", "true");
	p.addEntryWithValue("dbUDPPort", "-1");
	p.addEntryWithAlias("deleteLogins", "True");
	final Group toffile = p.addGroup("toffile");
	toffile.addEntryWithAlias("primary", "peHome");
	toffile.addEntryWithAlias("secondary", "peHome");
	final QueueGroup toDb = p.addQueue("toDb");
	toDb.addEntryWithAlias("Host", "host1");
	toDb.addEntryWithAlias("Port", "port1");
	toDb.addEntryWithAlias("QName", "DBRequest");
	toDb.addEntryWithAlias("QRouter", "router1");
	toDb.addEntryWithAlias("UserName", "userName1");
	toDb.addEntryWithAlias("QPassword", "password1");
	final String xml = serializeToString(p);
	System.out.println(xml);
  }

  @Test
  public void testDeserialization() throws Exception
  {
	final InputStream largeSample1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("large-sample1.xml");
	try
	{
	  final Properties p = deserializeFromStream(largeSample1);
	  assertEquals("localhost", p.getAlias("host4").getValue());
	  assertEquals("-1", p.getEntry("dbUDPPort").getValue());
	}
	finally
	{
	  largeSample1.close();
	}
  }

  private Properties deserializeFromStream(final InputStream stream) throws JAXBException
  {
	final JAXBContext jaxb = JAXBContext.newInstance(Properties.class);
	final Unmarshaller um = jaxb.createUnmarshaller();
	return Properties.class.cast(um.unmarshal(stream));
  }

  private String serializeToString(final Properties p) throws JAXBException, IOException
  {
	final JAXBContext jaxb = JAXBContext.newInstance(Properties.class);
	final Marshaller m = jaxb.createMarshaller();
	m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	final StringWriter writer = new StringWriter();
	try
	{
	  m.marshal(p, writer);
	  writer.flush();
	  return writer.toString();
	}
	finally
	{
	  writer.close();
	}
  }

}
