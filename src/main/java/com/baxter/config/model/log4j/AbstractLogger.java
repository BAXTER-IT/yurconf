package com.baxter.config.model.log4j;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public abstract class AbstractLogger
{

  @XmlElement(name = "level")
  private Level level;

  @XmlElement(name = "appender-ref")
  private List<AppenderRef> appenderRefs = new ArrayList<AppenderRef>();

  public Level getLevel()
  {
	return level;
  }

  public List<AppenderRef> getAppenderRefs()
  {
	return appenderRefs;
  }

  @XmlTransient
  public String getLevelValue()
  {
	if (this.level == null)
	{
	  return null;
	}
	else
	{
	  return this.level.getValue();
	}
  }

  public void setLevelValue(final String value)
  {
	final String levelValue = (value == null || value.isEmpty()) ? null : value;
	if (this.level == null)
	{
	  if (levelValue != null)
	  {
		this.level = new Level(levelValue);
	  }
	}
	else
	{
	  if (levelValue == null)
	  {
		this.level = null;
	  }
	  else
	  {
		this.level.setValue(levelValue);
	  }
	}
  }

  public abstract String getName();
  
  public abstract boolean isAdditivityIgnored();

  @XmlTransient
  public abstract boolean isAdditivity();
  
  public abstract void setAdditivity( final boolean value);

}
