/**
 * 
 */
package com.baxter.config.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Restful Servlet on Configuration Server. This is a main entry point to the configuration system for the clients. This
 * servlet processes the requests for configuration and delivers the configuration files.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 * 
 */
public class RestfulServlet extends HttpServlet
{

  /**
   * Serialization identifier.
   */
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
  {
	// TODO Auto-generated method stub
	super.doGet(request, response);
  }

}
