package com.baxter.config.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;

/**
 * Servlet implementation class MyTestServlet
 */

public class RestServlet extends HttpServlet
{

  private static final long serialVersionUID = 1L;

  /**
   * Default constructor.
   */
  public RestServlet()
  {
	// TODO Auto-generated constructor stub
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
	invoke(request, response);
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
  }

  private void invoke(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
	final String[] devidedUrl = request.getPathInfo().split("/");
	if (devidedUrl.length < 3)
	{
	  response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong request path. Should be: " + request.getContextPath()
		  + request.getServletPath() + "/<configurationType>/<componnentID>");
	  return;
	}

	final String requestConfigType = devidedUrl[1];
	final String requestComponentId = devidedUrl[2];
	final ConfigurationType configurationType;
	try
	{
	  configurationType = ConfigurationType.valueOf(requestConfigType);
	}
	catch (final IllegalArgumentException e)
	{
	  response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong configuration type " + requestConfigType);
	  return;
	}

	if (configurationType.isVersionAddicted())
	{
	  try
	  {
		if (!Version.isVersionSupported(request.getParameter("version")))
		{
		  response.sendError(
			  HttpServletResponse.SC_NOT_IMPLEMENTED,
			  "Server does not support version of configuration you requested. Highest supported version is "
			      + Version.getLatestVersion());
		  return;
		}
	  }
	  catch (final NumberFormatException e)
	  {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong or missed version parameter.");
		return;
	  }
	}

	final Component component;
	try
	{
	  component = Component.valueOf(requestComponentId);
	}
	catch (final IllegalArgumentException e)
	{
	  response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong component id " + requestComponentId);
	  return;
	}

	response.setContentType(configurationType.getContentType());
	final StoreManager storeManager = (StoreManager) getServletContext().getAttribute("storeManager");
	final InputStream is = storeManager.getInputStream(component.getFileName(configurationType));
	try
	{
	  BaxterConfigIOUtils.copy(is, response.getOutputStream(), configurationType, component);
	}
	catch (JAXBException e)
	{
	  e.printStackTrace();
	  IOUtils.copy(is, response.getOutputStream());
	}
	finally
	{
	  is.close();
	}
  }
}
