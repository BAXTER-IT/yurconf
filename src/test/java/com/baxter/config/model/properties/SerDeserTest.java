/**
 * 
 */
package com.baxter.config.model.properties;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Test;

import com.baxter.config.model.AbstractJaxbTest;

/**
 * @author ykryshchuk
 * 
 */
public class SerDeserTest extends AbstractJaxbTest
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
	  final Properties p = Properties.class.cast(deserializeFromStream(largeSample1));
	  assertEquals("localhost", p.getAlias("host4").getValue());
	  assertEquals("-1", p.getEntry("dbUDPPort").getValue());
	}
	finally
	{
	  largeSample1.close();
	}
  }

}
