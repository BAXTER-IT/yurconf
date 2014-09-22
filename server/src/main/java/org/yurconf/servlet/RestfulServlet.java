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

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yurconf.om.ConfigID;
import org.yurconf.om.ConfigParameter;
import org.yurconf.om.Version;
import org.yurconf.processor.AbstractProcessor;
import org.yurconf.processor.ProcessorContext;
import org.yurconf.processor.ProcessorException;

/**
 * The Restful Servlet on Configuration Server. This is a main entry point to
 * the configuration system for the clients. This servlet processes the requests
 * for configuration and delivers the configuration files.
 *
 * @author ykryshchuk
 * @since 1.5
 *
 */
public class RestfulServlet extends AbstractRepositoryServlet
{

  /**
   * Serialization identifier.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Version parameter name.
   */
  private static final String PARAM_VERSION = "version";

  @Override
  protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
  {
	logger.trace("New configuration request: {}?{}", request.getRequestURL(), request.getQueryString());
	final String pathInfo = request.getPathInfo();
	final String versionParam = request.getParameter(PARAM_VERSION);
	logger.debug("Request pathInfo {} and version {}", pathInfo, versionParam);
	try
	{
	  if (pathInfo == null)
	  {
		throw new IllegalArgumentException("Missing configuration path");
	  }
	  final Version version = Version.valueOf(versionParam);
	  final ConfigID configId = ConfigID.fromURLPath(pathInfo);
	  try
	  {
		final AbstractProcessor processor = getProcessorFactory().getProcessor(configId, version);
		try
		{
		  final ProcessorContext thisContext = new RequestProcessorContext(request, response, configId, pathInfo);
		  processor.process(thisContext);
		}
		catch (final ProcessorException e)
		{
		  logger.error("Processor failed", e);
		  response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	  }
	  catch (final ProcessorException e)
	  {
		logger.error("Could not get processor", e);
		response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
	  }
	}
	catch (final IllegalArgumentException e)
	{
	  logger.error("Failed to process request input", e);
	  response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
	}
  }

  /**
   * Processor context for servlet request.
   * @author ykryshchuk
   * @since 1.5
   */
  private final class RequestProcessorContext implements ProcessorContext
  {
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final List<ConfigParameter> params = new ArrayList<ConfigParameter>();
	private final ConfigID configId;
	private final String pathInfo;

	private RequestProcessorContext(final HttpServletRequest request, final HttpServletResponse response,
	    final ConfigID configId, final String pathInfo)
	{
	  this.request = request;
	  this.response = response;
	  this.configId = configId;
	  this.pathInfo = pathInfo;
	  for (final Enumeration<?> paramNames = request.getParameterNames(); paramNames.hasMoreElements();)
	  {
		final String parameterName = String.valueOf(paramNames.nextElement());
		if (!PARAM_VERSION.equals(parameterName))
		{
		  for (final String value : request.getParameterValues(parameterName))
		  {
			final ConfigParameter cParam = new ConfigParameter(parameterName, value);
			params.add(cParam);
		  }
		}
	  }
	  logger.debug("Parameters read from request: {}", params);
	}

	@Override
	public void setContentType(final String contentType, final String encoding)
	{
	  response.setContentType(contentType + ";charset=" + encoding);
	}

	@Override
	public OutputStream getOutputStream() throws IOException
	{
	  return response.getOutputStream();
	}

	@Override
	public ConfigID getConfigID()
	{
	  return configId;
	}

	@Override
	public URL getConfigurationBaseUrl()
	{
	  try
	  {
		return new URL(request.getRequestURL().toString().replace(pathInfo, ""));
	  }
	  catch (final MalformedURLException e)
	  {
		logger.error("Failed to construct configuration base URL", e);
		return null;
	  }
	}

	@Override
	public List<ConfigParameter> getParameters()
	{
	  return params;
	}
  }

}
