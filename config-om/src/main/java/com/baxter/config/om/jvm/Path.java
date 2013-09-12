/**
 * 
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
