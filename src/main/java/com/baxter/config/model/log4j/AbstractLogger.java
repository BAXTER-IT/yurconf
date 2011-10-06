package com.baxter.config.model.log4j;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

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

}
