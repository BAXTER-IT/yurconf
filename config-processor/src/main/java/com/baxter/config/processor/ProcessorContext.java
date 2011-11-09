/**
 * 
 */
package com.baxter.config.processor;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Processing context.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
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

}
