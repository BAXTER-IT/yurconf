/*
 * Yurconf Server
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.yurconf.processor.ProcessorFactory;

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
