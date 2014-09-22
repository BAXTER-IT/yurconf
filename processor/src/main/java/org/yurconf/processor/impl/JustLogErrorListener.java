/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.processor.impl;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Error listener that simply prints the error messages to logger.
 * @author ykryshchuk
 * @since 1.5
 */
final class JustLogErrorListener implements ErrorListener
{
  private static final ErrorListener INSTANCE = new JustLogErrorListener();

  protected final Logger logger;

  private JustLogErrorListener()
  {
	this.logger = LoggerFactory.getLogger(getClass());
  }

  static ErrorListener getInstance() {
	return INSTANCE;
  }

  @Override
  public void warning(final TransformerException exception) throws TransformerException
  {
	logger.warn("XSLT Warning: ", exception);
  }

  @Override
  public void fatalError(final TransformerException exception) throws TransformerException
  {
	logger.warn("XSLT Fatal: ", exception);
  }

  @Override
  public void error(final TransformerException exception) throws TransformerException
  {
	logger.warn("XSLT Error: ", exception);
  }
}
