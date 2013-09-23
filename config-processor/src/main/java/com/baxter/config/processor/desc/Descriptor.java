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

import com.baxter.config.om.Version;

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
  
  @XmlAttribute(name = "viewer", required = false)
  private String viewerStylesheet;

  @XmlElementWrapper(name = "processors")
  @XmlElement(name = "processor")
  private final List<Processor> processors = new ArrayList<Processor>();

  @XmlElementWrapper(name = "upgrades")
  @XmlElement(name = "from")
  private final List<Upgrade> upgrades = new ArrayList<Upgrade>();

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
	this.sourceUrl = url == null ? null : new URL(url, "./config/default/");
	this.xslUrl = url == null ? null : new URL(url, "./config/xsl/");
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
  
  public String getViewerStylesheet()
  {
	return viewerStylesheet;
  }

  /**
   * Returns the latest available upgrade from the specified version.
   * 
   * @param fromVersion
   *          version to be upgraded
   * @return the upgrade object
   */
  public Upgrade getLatestUpgrade(final String fromVersion)
  {
	Version maxToVersion = null;
	Upgrade latestUpgrade = null;
	for (final Upgrade upgrade : this.upgrades)
	{
	  if (fromVersion.equals(upgrade.getFromVersion()))
	  {
		final Version toVersion = Version.valueOf(upgrade.getToVersion());
		if (maxToVersion == null || maxToVersion.compareTo(toVersion) < 0)
		{
		  maxToVersion = toVersion;
		  latestUpgrade = upgrade;
		}
	  }
	}
	return latestUpgrade;
  }

  void setVersion(final String version)
  {
	this.version = version;
  }

  void setProductId(final String productId)
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
