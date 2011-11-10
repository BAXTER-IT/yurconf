/**
 * 
 */
package com.baxter.config.processor.desc;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Processor descriptor.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
@XmlRootElement(name = "descriptor", namespace = Descriptor.NS)
public class Descriptor
{

  /**
   * XML Namespace.
   */
  public static final String NS = "http://baxter-it.com/config/descriptor";

  @XmlTransient
  private URL url;

  @XmlAttribute(name = "version", required = true)
  private String version;

  @XmlAttribute(name = "productId", required = true)
  private String productId;

  @XmlElementWrapper(name = "processors", namespace = Descriptor.NS)
  @XmlElement(name = "processor", namespace = Descriptor.NS)
  private List<Processor> processors = new ArrayList<Processor>();

  public URL getUrl()
  {
	return url;
  }

  void setUrl(final URL url)
  {
	this.url = url;
  }

  public String getProductId()
  {
	return productId;
  }

  public String getVersion()
  {
	return version;
  }

  public List<Processor> getProcessors()
  {
	return processors;
  }

}
