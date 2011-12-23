/**
 * 
 */
package com.baxter.config.client;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import com.baxter.config.om.ConfigID;
import com.baxter.config.om.ConfigParameter;
import com.baxter.config.om.Version;

/**
 * The configuration request.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class Request
{

  private final URL url;
  private final ConfigID configId;
  private final Version version;
  private final ConfigParameter[] parameters;

  Request(final URL restUrl, final ConfigID configId, final Version version, final List<ConfigParameter> parameters)
	  throws MalformedURLException
  {
	this.configId = configId;
	this.version = version;
	this.parameters = parameters.toArray(new ConfigParameter[parameters.size()]);
	final String path = buildRequestPath(this.configId, this.version, this.parameters);
	// the context shall end with a slash, the spec thus must be a relative one
	this.url = new URL(restUrl, path);
  }

  private static String buildRequestPath(final ConfigID configId, final Version version, final ConfigParameter[] parameters)
  {
	final StringBuilder path = new StringBuilder(configId.toURLPath());
	if (version != null)
	{
	  path.append("?");
	  path.append("version").append("=").append(version);
	}
	if (parameters.length != 0)
	{
	  path.append(version == null ? "?" : "&");
	  for (int i = 0; i < parameters.length; i++)
	  {
		if (i != 0)
		{
		  path.append("&");
		  path.append(parameters[i].getName());
		  path.append("=");
		  try
		  {
			path.append(URLEncoder.encode(parameters[i].getValue(), "UTF-8"));
		  }
		  catch (final UnsupportedEncodingException e)
		  {
			throw new IllegalStateException("Cannot encode parameter value for URL", e);
		  }
		}
	  }
	}
	// return without leading slash
	return path.substring(1);
  }

  public URL getUrl()
  {
	return this.url;
  }

}
