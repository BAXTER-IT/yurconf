/**
 * 
 */
package com.baxter.config.client;

import java.net.URL;
import java.util.List;

import com.baxter.config.om.Version;

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
