/**
 * 
 */
package com.baxter.config.processor.desc;

import java.net.MalformedURLException;
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
@XmlRootElement(name = "descriptor")
public class Descriptor
{

  @XmlTransient
  private URL url;

  @XmlTransient
  private URL sourceUrl;

  @XmlTransient
  private URL xslUrl;

  @XmlAttribute(name = "version", required = true)
  private String version;

  @XmlAttribute(name = "productId", required = true)
  private String productId;

  @XmlElementWrapper(name = "processors")
  @XmlElement(name = "processor")
  private List<Processor> processors = new ArrayList<Processor>();

  public URL getUrl()
  {
	return url;
  }

  public URL getSourceUrl()
  {
	return sourceUrl;
  }

  public URL getXslUrl()
  {
	return xslUrl;
  }

  void setUrl(final URL url) throws MalformedURLException
  {
	this.url = url;
	this.sourceUrl = url == null ? null : new URL(url, "../config/default/");
	this.xslUrl = url == null ? null : new URL(url, "../config/xsl/");
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

  void setVersion(String version)
  {
	this.version = version;
  }

  void setProductId(String productId)
  {
	this.productId = productId;
  }

  @Override
  public String toString()
  {
	final StringBuilder str = new StringBuilder("Processor package [");
	str.append(getProductId());
	str.append(":");
	str.append(getVersion());
	str.append("]");
	return str.toString();
  }

}
