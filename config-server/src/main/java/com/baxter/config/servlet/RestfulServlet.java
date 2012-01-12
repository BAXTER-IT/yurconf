/**
 * 
 */
package com.baxter.config.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baxter.config.om.ConfigID;
import com.baxter.config.om.ConfigParameter;
import com.baxter.config.om.Version;
import com.baxter.config.processor.AbstractProcessor;
import com.baxter.config.processor.ProcessorContext;
import com.baxter.config.processor.ProcessorException;
import com.baxter.config.processor.ProcessorFactory;

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

  private static final String CTX_PARAM_REPOSITORY = "com.baxter.config.Repository";

  private static final String DEFAULT_REPO_PATH = System.getProperty("user.home") + File.separator
	  + ".baxter-configuration-repository";

  /**
   * Processor factory reference. This instance is initialized in {@link #init()} method.
   */
  private ProcessorFactory processorFactory;

  @Override
  public void init() throws ServletException
  {
	final ServletConfig servletConfig = getServletConfig();
	final String repositoryParam = servletConfig.getServletContext().getInitParameter(CTX_PARAM_REPOSITORY);
	LOGGER.debug("Parameter {} = {}", CTX_PARAM_REPOSITORY, repositoryParam);
	final String repositoryRootPath = (repositoryParam == null) ? DEFAULT_REPO_PATH : repositoryParam;
	try
	{
	  this.processorFactory = ProcessorFactory.getInstance(new File(repositoryRootPath));
	}
	catch (final ProcessorException e)
	{
	  LOGGER.error("Could not create ProcessorFactory", e);
	  throw new ServletException(e);
	}
  }

  @Override
  protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
  {
	LOGGER.trace("New configuration request: {}?{}", request.getRequestURL(), request.getQueryString() );
	final String pathInfo = request.getPathInfo();
	final String versionParam = request.getParameter(PARAM_VERSION);
	final List<ConfigParameter> params = new ArrayList<ConfigParameter>();
	for (Enumeration<?> paramNames = request.getParameterNames(); paramNames.hasMoreElements();)
	{
	  final String parameterName = String.valueOf(paramNames.nextElement());
	  if (!PARAM_VERSION.equals(parameterName))
	  {
		for (String value : request.getParameterValues(parameterName))
		{
		  final ConfigParameter cParam = new ConfigParameter(parameterName, value);
		  params.add(cParam);
		}
	  }
	}
	LOGGER.debug("Request pathInfo {} and version {}, params: {}", new Object[] { pathInfo, versionParam, params });
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
		final AbstractProcessor processor = processorFactory.getProcessor(configId, version);
		try
		{
		  final ProcessorContext thisContext = new ProcessorContext()
		  {

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
				LOGGER.error("Failed to construct configuration base URL", e);
				return null;
			  }
			}

			@Override
			public List<ConfigParameter> getParameters()
			{
			  return params;
			}

		  };
		  processor.process(thisContext);
		}
		catch (final ProcessorException e)
		{
		  LOGGER.error("Processor failed", e);
		  response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	  }
	  catch (final ProcessorException e)
	  {
		LOGGER.error("Could not get processor", e);
		response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
	  }
	}
	catch (final IllegalArgumentException e)
	{
	  LOGGER.error("Failed to process request input", e);
	  response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
	}
  }

}
