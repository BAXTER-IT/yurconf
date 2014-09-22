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
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yurconf.om.ConfigID;
import org.yurconf.om.ConfigParameter;
import org.yurconf.processor.AbstractProcessor;
import org.yurconf.processor.ConfigurationRepository;
import org.yurconf.processor.ProcessorContext;
import org.yurconf.processor.ProcessorException;
import org.yurconf.processor.desc.Descriptor;
import org.yurconf.processor.repo.RepositoryException;

/**
 * @author yura
 * @since 1.5
 */
public class ViewerServlet extends AbstractRepositoryServlet
{

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private static final Pattern PRODUCT_IN_PATH = Pattern.compile("/([^/]+)/?(.*)");

  @Override
  public void init() throws ServletException
  {
	super.init();
  }

  @Override
  protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
  {
	final String pathInfo = request.getPathInfo();
	logger.debug("Request pathInfo {}", pathInfo, pathInfo);
	if (pathInfo == null)
	{
	  final String requestUri = request.getRequestURI();
	  response.sendRedirect(requestUri + "/");
	}
	else if (pathInfo.equals("/"))
	{
	  try
	  {
		renderProductsList(response);
	  }
	  catch (final ProcessorException e)
	  {
		throw new ServletException(e);
	  }
	}
	else
	{
	  final Matcher m = PRODUCT_IN_PATH.matcher(pathInfo);
	  if (m.matches())
	  {
		final String productId = m.group(1);
		logger.trace("Request productId {}", productId);
		try
		{
		  final ProcessorContext context = new RequestProcessorContext(request, response, productId);
		  final AbstractProcessor processor = getProcessorFactory().getProcessor(context.getConfigID(), null);
		  try
		  {
			processor.process(context);
		  }
		  catch (final ProcessorException e)
		  {
			logger.error("Processor failed", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		  }

		}
		catch (final RepositoryException e)
		{
		  response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product not found " + productId);
		}
		catch (final ProcessorException e)
		{
		  response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product viewer cannot be loaded " + productId);
		}
	  }
	  else
	  {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URI");
	  }
	}
  }

  private void renderProductsList(final HttpServletResponse response) throws IOException, ProcessorException
  {
	final PrintWriter writer = response.getWriter();
	writer.println("<html>");
	writer.println("<head>");
	writer.println("<title>Products List - Configuration Server</title>");
	writer.println("</head>");
	writer.println("<body>");
	writer.println("<h1>Products List</h1>");
	final Iterator<Descriptor> descriptors = getRepository().getDescriptors();
	if (descriptors.hasNext())
	{
	  writer.println("<p>Below are all known products. Click any of items to see their available configurations.");
	  boolean foundSome = false;
	  writer.println("<ul>");
	  while (descriptors.hasNext())
	  {
		final Descriptor d = descriptors.next();
		final String viewer = d.getViewerStylesheet();
		if (viewer != null)
		{
		  writer.print("<li>");
		  writer.print("<a href=\"./" + d.getProductId() + "\">");
		  writer.print(d.getProductId() + " v. " + d.getVersion());
		  writer.print("</a>");
		  writer.println("</li>");
		  foundSome = true;
		}
		else
		{
		  writer.print("<li>");
		  writer.print(d.getProductId() + " v. " + d.getVersion());
		  writer.println("</li>");
		}
	  }
	  writer.println("</ul></p>");
	  if (!foundSome)
	  {
		writer.println("<p>Oops... there are some products found, but no one provides a viewer for you :(</p>");
	  }
	}
	else
	{
	  writer.println("<p>There are no products found :(<br/> Please install some configuration processors first.</p>");
	}
	writer.println("</body>");
	writer.println("</html>");
  }

  private ConfigurationRepository getRepository()
  {
	return getProcessorFactory().getRepository();
  }

  private final class RequestProcessorContext implements ProcessorContext
  {

	private final HttpServletResponse response;
	private final ConfigID configId;
	private final URL configBaseUrl;
	private final List<ConfigParameter> params = new ArrayList<ConfigParameter>();

	private RequestProcessorContext(final HttpServletRequest request, final HttpServletResponse response, final String productId)
	{
	  this.response = response;
	  final String variantsParam = request.getParameter("variants");
	  final List<String> variants = new ArrayList<String>();
	  if (variantsParam != null)
	  {
		for (final StringTokenizer tkn = new StringTokenizer(variantsParam, ",", false); tkn.hasMoreTokens();)
		{
		  variants.add(tkn.nextToken().trim());
		}
	  }
	  this.configId = new ConfigID(productId, "config-server", "viewer", variants);
	  try
	  {
		final URI uri = new URI(request.getRequestURL().toString()).resolve(getServletContext().getContextPath() + "/rest");
		this.configBaseUrl = uri.toURL();
	  }
	  catch (final Exception e)
	  {
		logger.error("Failed to determine config base url", e);
		throw new IllegalStateException(e);
	  }
	  for (final Enumeration<?> paramNames = request.getParameterNames(); paramNames.hasMoreElements();)
	  {
		final String parameterName = String.valueOf(paramNames.nextElement());
		if (!"version".equals(parameterName) && !"variants".equals(parameterName))
		{
		  for (final String value : request.getParameterValues(parameterName))
		  {
			final ConfigParameter cParam = new ConfigParameter(parameterName, value);
			params.add(cParam);
		  }
		}
	  }
	}

	@Override
	public OutputStream getOutputStream() throws IOException
	{
	  return response.getOutputStream();
	}

	@Override
	public void setContentType(final String contentType, final String encoding)
	{
	  response.setContentType(contentType);
	  response.setCharacterEncoding(encoding);
	}

	@Override
	public ConfigID getConfigID()
	{
	  return configId;
	}

	@Override
	public List<ConfigParameter> getParameters()
	{
	  return params;
	}

	@Override
	public URL getConfigurationBaseUrl()
	{
	  return configBaseUrl;
	}
  }

}
