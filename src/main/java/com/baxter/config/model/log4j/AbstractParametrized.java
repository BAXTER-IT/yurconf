package com.baxter.config.model.log4j;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public abstract class AbstractParametrized
{

  @XmlElement(name = "param")
  private List<Param> params = new ArrayList<Param>();

  @XmlAttribute(name = "class")
  private String className;

  public String getClassName()
  {
	return className;
  }

  public List<Param> getParams()
  {
    return params;
  }

}
