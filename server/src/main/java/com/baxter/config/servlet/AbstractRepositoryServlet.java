/*
 * Baxter Configuration Server
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baxter.config.processor.ProcessorFactory;

/**
 * @author yura
 * @since 1.5
 */
public abstract class AbstractRepositoryServlet extends HttpServlet
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * Logger instance.
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected ProcessorFactory getProcessorFactory() 
  {
	final ServletContext servletContext = getServletContext();
	return (ProcessorFactory) servletContext.getAttribute(ProcessorFactory.class.getName());
  }

}
