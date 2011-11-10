/**
 * 
 */
package com.baxter.config.processor.desc;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for Loader class.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class TestLoader
{

  @Test
  public void marshalToXml() throws Exception
  {
	final Descriptor d = new Descriptor();
	d.setVersion("1.1");
	d.setProductId("my.product");
	final List<Processor> processors = new ArrayList<Processor>();
	final Processor p1 = new Processor();
	p1.getParameters().add( new Parameter("n1","v1"));
	p1.getParameters().add( new Parameter("n2","v2"));
	processors.add(p1);
	d.getProcessors().add(p1);
	final Processor p2 = new Processor();
	p2.getParameters().add( new Parameter("name1","value1"));
	p2.getParameters().add( new Parameter("name2","value2"));
	processors.add(p2);
	d.getProcessors().add(p2);
	final String xml = marshal(d);
	assertNotNull(xml);
  }

  private String marshal(final Descriptor d) throws JAXBException
  {
	final JAXBContext jaxbContext = JAXBContext.newInstance(Descriptor.class);
	final Marshaller m = jaxbContext.createMarshaller();
	final StringWriter w = new StringWriter();
	m.marshal(d, w);
	return w.toString();
  }

}
