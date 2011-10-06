package com.baxter.config.servlet;

import java.io.FileInputStream;
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
	final String[] tokenizedURI = request.getRequestURI().split("/");
	if (tokenizedURI.length != 5)
	{
	  throw new ServletException("Wrong url");
	}
	final StoreManager storeManager = (StoreManager) getServletContext().getAttribute("storeManager");
//	final String pathToConfig = storeManager.getConfigurationURL(tokenizedURI[3], tokenizedURI[4]);
//	InputStream is = null;
//	try
//	{
//	  is = new FileInputStream(pathToConfig);
//	  IOUtils.copy(is, response.getOutputStream());
//	}
//	finally
//	{
//	  is.close();
//	}
  }
}
