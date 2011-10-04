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
@XmlRootElement(name = "group", namespace=Properties.NS)
public class Group extends AbstractContainer
{

  @XmlAttribute(name = "key")
  private String key;

  public Group()
  {
  }

  public Group(final String key)
  {
	this.key = key;
  }

  public String getKey()
  {
	return key;
  }

}
