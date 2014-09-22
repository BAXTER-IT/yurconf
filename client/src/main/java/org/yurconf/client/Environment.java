/*
 * Yurconf Client
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.client;

import java.net.URL;
import java.util.List;

import org.yurconf.om.Version;

/**
 * @author ykryshchuk
 * 
 */
public interface Environment
{

  String getProductId();

  String getComponentId();

  List<String> getVariants();

  Version getVersion();

  URL getRestUrl();

}
