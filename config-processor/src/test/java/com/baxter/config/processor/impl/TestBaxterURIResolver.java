/**
 * 
 */
package com.baxter.config.processor.impl;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.TransformerException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author yura
 * @sinceDevelopmentVersion
 */
public class TestBaxterURIResolver
{

  @Test
  public void resolveRelativeFromBaxterxsl() throws Exception
  {
	resolve("repo-base.xsl", "baxterxsl:jvm.xsl", "baxterxsl:repo-base.xsl");
  }

  @Test
  public void resolveRelativeFromProductBaxterxsl() throws Exception
  {
	resolve("repo-base.xsl", "baxterxsl:com.baxter.pe/jvm.xsl", "baxterxsl:com.baxter.pe/repo-base.xsl");
  }

  @Test
  public void resolveBaxterxslFromBaxterxsl() throws Exception
  {
	resolve("baxterxsl:1.xsl", "baxterxsl:2.xsl", "baxterxsl:1.xsl");
  }

  @Test
  public void resolveBaxterxslFromFile() throws Exception
  {
	resolve("baxterxsl:1.xsl", "file:/dir/file.xml", "baxterxsl:1.xsl");
  }

  private void resolve(final String href, final String base, final String expected) throws URISyntaxException,
	  TransformerException
  {
	final BaxterURIResolver r = new BaxterURIResolver(null);
	final URI hrefUri = new URI(href);
	final URI baseUri = new URI(base);
	final URI resolved = r.resolve(hrefUri, baseUri);
	Assert.assertEquals("Resolved wrong URI", new URI(expected), resolved);
  }

}
