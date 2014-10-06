package org.yurconf.demo.om;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JaxbTest
{

  @Test
  public void tryMarshalling() throws Exception
  {
	final Greeting g = new Greeting(GreetingType.HELLO, "Maven Runner");
	final JAXBContext jc = JAXBContext.newInstance("org.yurconf.demo.om");
	final Marshaller m = jc.createMarshaller();
	final StringWriter writer = new StringWriter();
	m.marshal(g, writer);
	assertEquals(
	    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><greeting as=\"HELLO\" xmlns=\"http://demo.yurconf.org/om\">Maven Runner</greeting>",
	    writer.toString());
  }

}
