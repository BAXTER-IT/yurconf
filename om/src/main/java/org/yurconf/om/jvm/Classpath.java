/*
 * Baxter Configuration Object Model
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
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
