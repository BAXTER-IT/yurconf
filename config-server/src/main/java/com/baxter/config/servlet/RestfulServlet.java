/**
 * 
 */
package com.baxter.config.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baxter.config.om.ConfigID;
import com.baxter.config.om.Version;

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
  
  /**
   * Logger instance.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(RestfulServlet.class);
  
  /**
   * Version parameter name.
   */
  private static final String PARAM_VERSION = "version";

  @Override
  protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
  {
	final String pathInfo = request.getPathInfo();
	final String versionParam = request.getParameter(PARAM_VERSION);
	LOGGER.trace("Request pathInfo {} and version {}", pathInfo, versionParam);
	try {
	  if ( pathInfo == null ) {
		throw new IllegalArgumentException("Missing configuration path");
	  }
	  final Version version = Version.valueOf(versionParam);
	  final ConfigID configId = ConfigID.fromURLPath(pathInfo);
	  
		// TODO Auto-generated method stub
		super.doGet(request, response);
	} catch ( final IllegalArgumentException e ) {
	  LOGGER.error("Failed to process request input", e);
	  response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
	}
  }

}
