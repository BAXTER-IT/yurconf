/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.processor.impl;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.TransformerException;

import org.junit.Assert;
import org.junit.Test;
import org.yurconf.processor.impl.YurconfUriResolver;

/**
 * @author yura
 * @since 1.5
 */
public class TestYurconfUriResolver
{

  @Test
  public void resolveRelativeFromYurconf() throws Exception
  {
	resolve("repo-base.xsl", "yurconf://org.yurconf.java/jvm.xsl", "yurconf://org.yurconf.java/repo-base.xsl");
  }

  @Test
  public void resolveYurconfFromYurconf() throws Exception
  {
	resolve("yurconf://proba/1.xsl", "yurconf://predefined/2.xsl", "yurconf://proba/1.xsl");
  }

  @Test
  public void resolveYurconfFromFile() throws Exception
  {
	resolve("yurconf://vendor/1.xsl", "file:/dir/file.xml", "yurconf://vendor/1.xsl");
  }

  private void resolve(final String href, final String base, final String expected) throws URISyntaxException,
	  TransformerException
  {
	final YurconfUriResolver r = new YurconfUriResolver(null);
	final URI hrefUri = new URI(href);
	final URI baseUri = new URI(base);
	final URI resolved = r.resolve(hrefUri, baseUri);
	Assert.assertEquals("Resolved wrong URI", new URI(expected), resolved);
  }

}
