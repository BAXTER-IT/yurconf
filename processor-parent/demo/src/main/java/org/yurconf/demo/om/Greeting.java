package org.yurconf.demo.om;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author yura
 * @sinceDevelopmentVersion
 */
@XmlRootElement(name = "greeting")
public class Greeting
{

  @XmlAttribute(name = "as", required = true)
  private GreetingType type;

  @XmlValue
  private String name;

  public Greeting()
  {
  }

  public Greeting(final GreetingType type, final String name)
  {
	this.type = type;
	this.name = name;
  }

  public GreetingType getType()
  {
	return type;
  }

  public String getName()
  {
	return name;
  }

}
