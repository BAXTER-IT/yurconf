/**
 *
 */
package org.yurconf.servlet;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.mockito.Mockito;

/**
 * @author yura
 * @sinceDevelopmentVersion
 */
public class PingServletTest
{

  @Test
  public void doGet() throws Exception
  {
	final PingServlet servlet = new PingServlet();
	final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
	Mockito.doReturn("junit").when(request).getParameter("origin");
	final StringWriter writer = new StringWriter();
	final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	Mockito.doReturn(new PrintWriter(writer)).when(response).getWriter();
	servlet.doGet(request, response);
	Mockito.verify(response).setContentType("text/plain");
	final Pattern pattern = Pattern.compile("Ping from ([^\\s]+) arrived on \\d+");
	final Matcher m = pattern.matcher(writer.toString());
	assertTrue(m.matches());
	assertEquals("junit", m.group(1));
  }

}
