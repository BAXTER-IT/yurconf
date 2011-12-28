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
	this.url = buildRequestURL(restUrl, this.configId, this.version, this.parameters);
  }

  private static URL buildRequestURL(final URL restUrl, final ConfigID configId, final Version version,
	  final ConfigParameter[] parameters) throws MalformedURLException
  {
	final StringBuilder path = new StringBuilder(restUrl.toString());
	if (path.charAt(path.length() - 1) == '/')
	{
	  path.deleteCharAt(path.length() - 1);
	}
	path.append(configId.toURLPath());
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
		}
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
	return new URL(path.toString());
  }

  public URL getUrl()
  {
	return this.url;
  }

}
