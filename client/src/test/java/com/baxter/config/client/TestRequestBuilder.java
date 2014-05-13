/**
 *
 */
package com.baxter.config.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.Arrays;

import org.junit.Test;

import com.baxter.config.om.Version;

/**
 * @author ykryshchuk
 *
 */
public class TestRequestBuilder
{

  @Test
  public void buildWithType() throws Exception
  {
	final Environment env = mock(Environment.class);
	when(env.getProductId()).thenReturn("dummy.id");
	when(env.getComponentId()).thenReturn("mocked");
	when(env.getRestUrl()).thenReturn(new URL("http://dummy/rest/"));
	when(env.getVersion()).thenReturn(Version.valueOf("1.1"));
	when(env.getVariants()).thenReturn(Arrays.asList("dummy,mock"));

	final RequestBuilder b = new RequestBuilder().forType("simple");

	final Request r = b.createRequest(env);
	assertEquals(new URL("http://dummy/rest/dummy.id/mocked/dummy,mock/simple?version=1.1"), r.getUrl());
  }

  @Test
  public void buildWithType2() throws Exception
  {
	final Environment env = mock(Environment.class);
	when(env.getProductId()).thenReturn("dummy.id");
	when(env.getComponentId()).thenReturn("mocked");
	when(env.getRestUrl()).thenReturn(new URL("http://dummy/rest/"));
	when(env.getVersion()).thenReturn(null);
	when(env.getVariants()).thenReturn(Arrays.asList("dummy,mock"));

	final RequestBuilder b = new RequestBuilder().forType("simple");

	final Request r = b.createRequest(env);
	assertEquals(new URL("http://dummy/rest/dummy.id/mocked/dummy,mock/simple"), r.getUrl());
  }

  @Test
  public void requestWithParameter() throws Exception {
	final Environment env = mock(Environment.class);
	when(env.getProductId()).thenReturn("dummy.id");
	when(env.getComponentId()).thenReturn("mocked");
	when(env.getRestUrl()).thenReturn(new URL("http://dummy/rest/"));
	when(env.getVersion()).thenReturn(null);
	when(env.getVariants()).thenReturn(Arrays.asList("def"));

	final RequestBuilder b = new RequestBuilder().forType("asis").addParameter("file", "constants.license");

	final Request r = b.createRequest(env);
	assertEquals(new URL("http://dummy/rest/dummy.id/mocked/def/asis?file=constants.license"), r.getUrl());
  }

  @Test
  public void requestWithParameters() throws Exception {
	final Environment env = mock(Environment.class);
	when(env.getProductId()).thenReturn("dummy.id");
	when(env.getComponentId()).thenReturn("mocked");
	when(env.getRestUrl()).thenReturn(new URL("http://dummy/rest/"));
	when(env.getVersion()).thenReturn(null);
	when(env.getVariants()).thenReturn(Arrays.asList("def"));

	final RequestBuilder b = new RequestBuilder().forType("asis").addParameter("file", "constants.license").addParameter("local","yes");

	final Request r = b.createRequest(env);
	assertEquals(new URL("http://dummy/rest/dummy.id/mocked/def/asis?file=constants.license&local=yes"), r.getUrl());
  }

  @Test
  public void prependVariant() throws Exception {
	final Environment env = mock(Environment.class);
	when(env.getProductId()).thenReturn("dummy.id");
	when(env.getComponentId()).thenReturn("mocked");
	when(env.getRestUrl()).thenReturn(new URL("http://dummy/rest/"));
	when(env.getVersion()).thenReturn(null);
	when(env.getVariants()).thenReturn(Arrays.asList("def1","def2"));

	final RequestBuilder b = new RequestBuilder().prependVariant("prep").forType("blah");

	final Request r = b.createRequest(env);
	assertEquals(new URL("http://dummy/rest/dummy.id/mocked/prep,def1,def2/blah"), r.getUrl());
  }

}
