/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 *
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 *
 * Join our team to help make it better.
 */
package org.yurconf.processor.desc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;
import org.yurconf.processor.desc.Descriptor;
import org.yurconf.processor.desc.Loader;
import org.yurconf.processor.desc.Parameter;
import org.yurconf.processor.desc.Processor;
import org.yurconf.processor.desc.Upgrade;
import org.yurconf.processor.desc.UpgradeAddFile;
import org.yurconf.processor.desc.UpgradeMoveFile;
import org.yurconf.processor.desc.UpgradeRemoveFile;

/**
 * Test suite for Loader class.
 *
 * @author ykryshchuk
 * @since 1.5
 */
public class TestLoader
{

  @Test
  public void loadDescriptorFromResource1() throws Exception
  {
	final URL url = getClass().getResource("data/test-descriptor-1.xml");
	final Descriptor d = Loader.getInstance().load(url, false);
	assertEquals("com.baxter.test", d.getProductId());
	assertEquals("22.33", d.getVersion());
	assertEquals(2, d.getProcessors().size());
	assertEquals("org.yurconf.processor.impl.XSLTProcessor", d.getProcessors().get(0).getClassName());
	assertEquals("proba", d.getProcessors().get(0).getConfigurationType());
  }

  @Test
  public void loadDescriptorFromUpgrade() throws Exception
  {
	final URL url = getClass().getResource("data/test-descriptor-upgrade.xml");
	final Descriptor d = Loader.getInstance().load(url, true);

	final Upgrade from0_9 = d.getLatestUpgrade("0.9");
	assertNull(from0_9);

	final Upgrade from1_0 = d.getLatestUpgrade("1.0");
	assertEquals("2.0", from1_0.getToVersion());
	assertEquals(UpgradeAddFile.class, from1_0.getCommands().get(0).getClass());
	assertEquals(UpgradeAddFile.class, from1_0.getCommands().get(1).getClass());
	assertEquals(UpgradeRemoveFile.class, from1_0.getCommands().get(2).getClass());
	assertEquals(UpgradeMoveFile.class, from1_0.getCommands().get(3).getClass());

	final Upgrade from2_0 = d.getLatestUpgrade("2.0");
	assertEquals("3.0", from2_0.getToVersion());

	final Upgrade from2_1 = d.getLatestUpgrade("2.1");
	assertEquals("3.0", from2_1.getToVersion());
  }

  @Test
  public void marshalToXml() throws Exception
  {
	final Descriptor d = new Descriptor();
	d.setVersion("1.1");
	d.setProductId("my.product");
	final List<Processor> processors = new ArrayList<Processor>();
	final Processor p1 = new Processor();
	p1.setClassName("org.yurconf.processor.XSLTProcessor");
	p1.setConfigurationType("simple");
	p1.getParameters().add(new Parameter("n1", "v1"));
	p1.getParameters().add(new Parameter("n2", "v2"));
	processors.add(p1);
	d.getProcessors().add(p1);
	final Processor p2 = new Processor();
	p2.setClassName("org.yurconf.processor.AsIsProcessor");
	p2.setConfigurationType("advanced");
	p2.getParameters().add(new Parameter("name1", "value1"));
	p2.getParameters().add(new Parameter("name2", "value2"));
	processors.add(p2);
	d.getProcessors().add(p2);
	final String xml = marshal(d);
	System.out.println("Marshalled Descriptor: " + xml);
	assertNotNull(xml);
  }

  private String marshal(final Descriptor d) throws JAXBException
  {
	final JAXBContext jaxbContext = Loader.getInstance().getJaxbContext();
	final Marshaller m = jaxbContext.createMarshaller();
	final StringWriter w = new StringWriter();
	m.marshal(d, w);
	return w.toString();
  }

}
