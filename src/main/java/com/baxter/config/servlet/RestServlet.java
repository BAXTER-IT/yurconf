package com.baxter.config.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	System.out.println(request.getParameter("version"));
	final String[] devidedUrl = request.getPathInfo().split("/");
	if (devidedUrl.length != 3)
	{
	  response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong request path.\n Should be: " + request.getContextPath()
		  + request.getServletPath() + "/<configurationType>/<componnentID>");
	  return;
	}
	final String requestConfigType = devidedUrl[1];
	final String requestComponnentId = devidedUrl[2];
	final ConfigurationType configurationType;
	try
	{
	  configurationType = ConfigurationType.valueOf(requestConfigType);
	}
	catch (final IllegalArgumentException e)
	{
	  response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong configuration type <" + requestConfigType + ">");
	  return;
	}
	response.setContentType(configurationType.getContentType());
	final StoreManager storeManager = (StoreManager) getServletContext().getAttribute("storeManager");
	final InputStream is = storeManager.getInputStream(configurationType.getFileName(requestComponnentId));
	try
	{
	  IOUtils.copy(is, response.getOutputStream());
	}
	finally
	{
	  is.close();
	}
  }
}
