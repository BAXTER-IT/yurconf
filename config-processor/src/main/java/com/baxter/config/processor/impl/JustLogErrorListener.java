package com.baxter.config.processor.impl;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JustLogErrorListener implements ErrorListener
{
  protected final Logger logger; 
  
  public JustLogErrorListener()
  {
	this.logger = LoggerFactory.getLogger(getClass());
  }

  @Override
  public void warning(final TransformerException exception) throws TransformerException
  {
	logger.warn("XSLT Warning", exception);
  }

  @Override
  public void fatalError(final TransformerException exception) throws TransformerException
  {
	logger.warn("XSLT Fatal", exception);
  }

  @Override
  public void error(final TransformerException exception) throws TransformerException
  {
	logger.warn("XSLT Error", exception);
  }
}
