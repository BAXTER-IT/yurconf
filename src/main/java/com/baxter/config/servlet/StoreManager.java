package com.baxter.config.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

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

  public void untag(final String tag, final Messages msg)
  {
	final File tagDir = new File(this.configRoot, tag == null ? "default" : tag);
	if (!tagDir.exists())
	{
	  if (msg != null)
	  {
		msg.add("Tag not found");
	  }
	  else
	  {
		System.err.println("Tag not found");
	  }
	}
	else
	{
	  try
	  {
		if (!tagDir.equals(defaultTag))
		{
		  FileUtils.copyDirectory(tagDir, defaultTag);
		}
	  }
	  catch (final IOException e)
	  {
		if (msg != null)
		{
		  msg.add(e);
		}
		else
		{
		  e.printStackTrace();
		}
	  }
	}
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

  public InputStream getInputStream(final String resource) throws IOException
  {
	final File resourceFile = new File(defaultTag, resource);
	if (resourceFile.isFile())
	{
	  return new FileInputStream(resourceFile);
	}
	else
	{
	  throw new FileNotFoundException("No such resource");
	}
  }

  public OutputStream getOutputStream(final String resource) throws IOException
  {
	final File resourceFile = new File(this.defaultTag, resource);
	return new FileOutputStream(resourceFile);
  }

  public List<String> getStoredTags()
  {
	return Arrays.asList(this.configRoot.list());
  }

  public String getConfigurationURL(final String configType, final String componentId)
  {
	final StringBuilder path = new StringBuilder(this.configRoot.getAbsolutePath());
	path.append("/default/");
	path.append(configType);
	if (configType.equals("log4j"))
	{
	  path.append("_").append(componentId);
	}
	path.append(".xml");
	return path.toString();
  }

  public void copyFileStructure() throws IOException
  {
	copyConfigFile("properties.xml");
	copyConfigFile("log4j_DBServer.xml");
	copyConfigFile("log4j_Broadcast.xml");
	copyConfigFile("log4j_BlotterServer.xml");
	copyConfigFile("log4j_BlotterClient.xml");
  }

  private void copyConfigFile(final String fileName) throws IOException
  {
	final URL url = StoreManager.class.getResource("/.baxter-config/default/" + fileName);
	final File destination = new File(defaultTag, fileName);
	if (!destination.exists())
	{
	  destination.createNewFile();
	  OutputStream os = null;
	  InputStream is = null;
	  try
	  {
		is = url.openStream();
		os = new FileOutputStream(destination);
		IOUtils.copy(is, os);
	  }
	  finally
	  {
		os.close();
		is.close();
	  }

	}
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
