package com.baxter.config.model.log4j;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Test;

import com.baxter.config.model.AbstractJaxbTest;
import com.baxter.config.model.log4j.Configuration;

public class SerDeserTest extends AbstractJaxbTest
{

  @Test
  public void testSerialization() throws Exception
  {
	final Configuration lconf = new Configuration();
	
	final String xml = serializeToString(lconf);
	System.out.println(xml);
  }

  @Test
  public void testDeserialization() throws Exception
  {
	final InputStream sample1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("log4j-sample1.xml");
	try
	{
	  final Configuration c = Configuration.class.cast(deserializeFromStream(sample1));
	  assertEquals("INFO", c.getRoot().getLevel().getValue() );
	  assertEquals("CONSOLE", c.getRoot().getAppenderRefs().get(0).getRef() );
	  assertEquals("CONSOLE", c.getAppenders().get(0).getName() );
	  assertEquals("Target", c.getAppenders().get(0).getParams().get(0).getName() );
	  assertEquals("System.out", c.getAppenders().get(0).getParams().get(0).getValue() );
	}
	finally
	{
	  sample1.close();
	}
  }
}
