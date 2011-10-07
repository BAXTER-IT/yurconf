package com.baxter.config.model.log4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Appender extends AbstractParametrized implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  @XmlAttribute(name = "name")
  private String name;

  @XmlElement(name = "layout")
  private Layout layout;

  @XmlElement(name = "appender-ref")
  private List<AppenderRef> appenderRefs = new ArrayList<AppenderRef>();

  public String getName()
  {
	return name;
  }

  public Layout getLayout()
  {
	return layout;
  }

  public List<AppenderRef> getAppenderRefs()
  {
	return appenderRefs;
  }

}
