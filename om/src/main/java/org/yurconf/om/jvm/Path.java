/*
 * Yurconf Object Model
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.om.jvm;

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
