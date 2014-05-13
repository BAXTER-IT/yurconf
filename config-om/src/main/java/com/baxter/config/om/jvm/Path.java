/*
 * Baxter Configuration Object Model
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.om.jvm;

import java.net.URL;

import javax.xml.bind.annotation.XmlValue;

/**
 * @author ykryshchuk
 *
 */
public class Path
{

  @XmlValue
  private URL url;

  public Path()
  {
  }

  Path(final URL url)
  {
	this.url = url;
  }

  public URL getUrl()
  {
	return url;
  }

}
