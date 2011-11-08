package com.baxter.config.model.properties;

import javax.xml.bind.annotation.XmlAttribute;

public class Import
{
  @XmlAttribute(name = "resource")
  private String resource;

  @XmlAttribute(name = "writeable")
  private boolean writeable;

  public Import()
  {

  }

  public Import(final String resource, final boolean writeable)
  {
	this.resource = resource;
	this.writeable = writeable;
  }

  @Override
  public String toString()
  {
	return "Import " + resource + " " + writeable;
  }
}
