package com.baxter.config.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.baxter.config.bean.Messages;

public class StoreManager
{

  private final File configRoot;

  private final File defaultTag;

  public StoreManager()
  {
	final File userHome = new File(System.getProperty("user.home"));
	this.configRoot = ensureDirExists(new File(userHome, ".baxter-config"));
	this.defaultTag = ensureDirExists(new File(this.configRoot, "default"));
  }

  public void tag(final String tag, final Messages msg)
  {
	final File tagDir = new File(this.configRoot, tag);
	if (tagDir.exists())
	{
	  msg.add("Tag already used");
	}
	else
	{
	  try
	  {
		FileUtils.copyDirectory(defaultTag, tagDir);
	  }
	  catch (final IOException e)
	  {
		msg.add(e);
	  }
	}
  }

  public List<String> getStoredTags()
  {
	return Arrays.asList(this.configRoot.list());
  }

  public String getConfigurationURL(String configType, String componentId)
  {
	final StringBuilder path = new StringBuilder(this.configRoot.getAbsolutePath());
	path.append("/");
	path.append(configType).append("/");
	path.append(configType);
	path.append(".xml");
	return path.toString();
  }

  private static File ensureDirExists(final File dir)
  {
	if (!dir.isDirectory())
	{
	  if (dir.exists())
	  {
		if (!dir.delete())
		{
		  throw new IllegalStateException("Could not renew directory " + dir.getAbsolutePath());
		}
	  }
	  if (!dir.mkdirs())
	  {
		throw new IllegalStateException("Could not create directory " + dir.getAbsolutePath());
	  }
	}
	return dir;
  }

}
