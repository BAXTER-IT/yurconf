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
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PingServlet extends HttpServlet
{

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException
  {
	String origin = req.getParameter("origin");
	resp.setContentType("text/plain");
	PrintWriter writer = resp.getWriter();
	writer.format("Ping from %1$s arrived on %2$s", origin, System.currentTimeMillis());
	writer.flush();
  }

}
