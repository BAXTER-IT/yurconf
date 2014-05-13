/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.processor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import com.baxter.config.om.ConfigID;
import com.baxter.config.om.ConfigParameter;

/**
 * Processing context.
 *
 * @author ykryshchuk
 * @since 1.5
 */
public interface ProcessorContext
{

  /**
   * Returns the output stream for current context.
   *
   * @return output stream
   * @throws IOException
   *           if cannot return the stream
   */
  OutputStream getOutputStream() throws IOException;

  /**
   * Sets the content type for processing result.
   *
   * @param contentType
   *          content type, like "text/xml"
   * @param encoding
   *          result encoding, like "UTF-8"
   */
  void setContentType(String contentType, String encoding);

  /**
   * Returns the configuration identifier for this context.
   *
   * @return configuration identifier
   */
  ConfigID getConfigID();

  /**
   * Returns configuration parameters.
   *
   * @return list of configuration parameters
   */
  List<ConfigParameter> getParameters();

  /**
   * Returns the base URL for configurations. This is typically the URL to restful servlet.
   * @return
   */
  URL getConfigurationBaseUrl();

}
