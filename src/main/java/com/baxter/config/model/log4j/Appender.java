package com.baxter.config.model.log4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

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

  public void setOutput(final String output)
  {
	try
	{
	  getParam("File").setValue(output);
	}
	catch (final NoSuchElementException e)
	{
	  // nowhere to set
	}
  }

  @XmlTransient
  public String getOutput()
  {
	try
	{
	  return getParam("File").getValue();
	}
	catch (final NoSuchElementException e)
	{
	  try
	  {
		return getParam("Target").getValue();
	  }
	  catch (final NoSuchElementException e2)
	  {
		System.out.println("Tried to get Target after File, and failed");
		return null;
	  }
	}
  }

  public boolean isFileAppender()
  {
	try
	{
	  getParam("File");
	  return true;
	}
	catch (final NoSuchElementException e)
	{
	  return false;
	}
  }

}
