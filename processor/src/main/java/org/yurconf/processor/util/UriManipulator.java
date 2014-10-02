package org.yurconf.processor.util;

import java.net.URI;
import java.net.URISyntaxException;

public class UriManipulator
{

  static final String JAR_URI_SCHEME = "jar";

  private UriManipulator()
  {
  }

  public static URI resolve(final URI uri, final URI path) throws URISyntaxException
  {
	if (JAR_URI_SCHEME.equals(uri.getScheme()) && !path.isAbsolute())
	{
	  final String jarPath = uri.getSchemeSpecificPart();
	  final String pathString = path.toString();
	  if (pathString.charAt(0) == '/')
	  {
		final int pathEnd = jarPath.indexOf('!');
		return new URI(JAR_URI_SCHEME, jarPath.substring(0, pathEnd + 1).concat(pathString), null);
	  }
	  else
	  {
		return new URI(JAR_URI_SCHEME, URI.create(jarPath).resolve(path).toString(), null);
	  }
	}
	else
	{
	  return uri.resolve(path);
	}
  }

  public static URI resolve(final URI uri, final String path) throws URISyntaxException
  {
	return resolve(uri, URI.create(path));
  }

}
