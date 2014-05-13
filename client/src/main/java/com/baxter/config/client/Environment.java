/*
 * Baxter Configuration Client
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
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
