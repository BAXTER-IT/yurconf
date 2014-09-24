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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.yurconf.om.Version;

/**
 * Processor descriptor.
 *
 * @author ykryshchuk
 * @since 1.5
 */
@XmlRootElement(name = "descriptor")
public class Descriptor
{

  public static final String PROCESSOR_XML_RESOURCE = "META-INF/org.yurconf.processor.xml";

  @XmlTransient
  private URL url;

  @XmlTransient
  private URI rootUri;

  @XmlTransient
  private URI defaultSourceUri;

  @XmlAttribute(name = "version", required = true)
  private String version;

  @XmlAttribute(name = "productId", required = true)
  private String productId;

  @XmlAttribute(name = "viewer", required = false)
  private String viewerStylesheet;

  @XmlAttribute(name = "root", required = false)
  private String resourceRoot;

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

  public URI getRootUri()
  {
	return rootUri;
  }

  public URI getDefaultSourceUri()
  {
	return defaultSourceUri;
  }

  void setUrl(final URL url) throws URISyntaxException
  {
	if (url == null)
	{
	  throw new IllegalArgumentException("Cannot use processor descriptor URL as null");
	}
	this.url = url;
	final URI packageRoot = url.toURI().resolve("../").normalize();
	this.rootUri = (resourceRoot == null || resourceRoot.isEmpty()) ? packageRoot : packageRoot.resolve(resourceRoot);
	this.defaultSourceUri = this.rootUri.resolve("META-INF/default");
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
