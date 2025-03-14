/**
 * 
 */
package com.baxter.config.processor.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.TransformerException;

import org.junit.jupiter.api.Test;

/**
 * @author yura
 * @sinceDevelopmentVersion
 */
class TestBaxterURIResolver
{

  @Test
  void resolveRelativeFromBaxterxsl() throws Exception
  {
	resolve("repo-base.xsl", "baxterxsl:jvm.xsl", "baxterxsl:repo-base.xsl");
  }

  @Test
  void resolveRelativeFromProductBaxterxsl() throws Exception
  {
	resolve("repo-base.xsl", "baxterxsl:com.baxter.pe/jvm.xsl", "baxterxsl:com.baxter.pe/repo-base.xsl");
  }

  @Test
  void resolveBaxterxslFromBaxterxsl() throws Exception
  {
	resolve("baxterxsl:1.xsl", "baxterxsl:2.xsl", "baxterxsl:1.xsl");
  }

  @Test
  void resolveBaxterxslFromFile() throws Exception
  {
	resolve("baxterxsl:1.xsl", "file:/dir/file.xml", "baxterxsl:1.xsl");
  }

  private void resolve(final String href, final String base, final String expected)
      throws URISyntaxException, TransformerException
  {
	final BaxterURIResolver r = new BaxterURIResolver(null);
	final URI hrefUri = new URI(href);
	final URI baseUri = new URI(base);
	final URI resolved = r.resolve(hrefUri, baseUri);
	assertEquals(new URI(expected), resolved, "Resolved wrong URI");
  }

}
