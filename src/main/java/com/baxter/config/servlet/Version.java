package com.baxter.config.servlet;

public class Version
{
  public static String getLatestVersion()
  {
	return "2.0.1";
  }

  public static boolean isVersionSupported(final String requestedVersion) throws NumberFormatException
  {
	final String[] requestedVersionDevided = requestedVersion.split("\\.");
	final String[] supportedVersionDevided = getLatestVersion().split("\\.");
	final int smallestLength = supportedVersionDevided.length >= requestedVersionDevided.length ? requestedVersionDevided.length
	    : supportedVersionDevided.length;
	for (int i = 0; i < smallestLength; i++)
	{
	  if (Integer.valueOf(supportedVersionDevided[i]) > Integer.valueOf(requestedVersionDevided[i]))
	  {
		return true;
	  }
	  else if (Integer.valueOf(supportedVersionDevided[i]) < Integer.valueOf(requestedVersionDevided[i]))
	  {
		return false;
	  }
	}

	if (supportedVersionDevided.length >= requestedVersionDevided.length)
	{
	  return true;
	}
	return false;
  }
}
