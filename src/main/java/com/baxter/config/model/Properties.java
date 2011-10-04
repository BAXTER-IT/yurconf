/**
 * 
 */
package com.baxter.config.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ykryshchuk
 * 
 */
@XmlRootElement(name = "properties", namespace = Properties.NS)
public class Properties extends AbstractContainer
{
  static final String NS = "http://baxter.com/config";

  @XmlAttribute(name = "version")
  private String version = "1.1";

  public String getVersion()
  {
	return version;
  }

  public int getJMSInstancesCount()
  {
	return 3;
  }

  public String getLoadFrom()
  {
	return null;
  }

  public void setLoadFrom(final String where)
  {
	System.out.println("Loading from " + where);
  }

}
