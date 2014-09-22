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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ykryshchuk
 * 
 */
@XmlRootElement(name = "classpath")
public class Classpath
{

  @XmlElement(name = "path")
  private final List<Path> paths = new ArrayList<Path>();

  public boolean isEmpty()
  {
    return paths.isEmpty();
  }

  void add(final Path path)
  {
    paths.add(path);
  }

  public URL[] getUrls()
  {
    final URL[] urls = new URL[paths.size()];
    int i = 0;
    for (final Path p : paths)
    {
      urls[i++] = p.getUrl();
    }
    return urls;
  }

}
