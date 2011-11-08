package com.baxter.config.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.baxter.config.bean.Messages;

public class StoreManager
{

  public static final String TAG_DEEFAULT = "default";

  private final File configRoot;

  private final File defaultTag;

  public StoreManager()
  {
	final File userHome = new File(System.getProperty("user.home"));
	this.configRoot = ensureDirExists(new File(userHome, ".baxter-config"));
	this.defaultTag = ensureDirExists(new File(this.configRoot, TAG_DEEFAULT));
  }

  public void untag(final String tag, final Messages msg)
  {
	if (tag != null && !TAG_DEEFAULT.equals(tag))
	{
	  final File tagDir = new File(this.configRoot, tag);
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
		  FileUtils.copyDirectory(tagDir, defaultTag);
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

  public OutputStream getOutputStream(final String resource, final boolean failOnExists) throws IOException
  {
	final File resourceFile = new File(this.defaultTag, resource);
	if (failOnExists && resourceFile.exists())
	{
	  throw new FileAlreadyExistsException(resourceFile);
	}
	return new FileOutputStream(resourceFile);
  }

  public List<String> getStoredTags()
  {
	return Arrays.asList(this.configRoot.list());
  }

  public void copyFileStructure()
  {
	try
	{
	  copyConfigFile("properties.xml");
	  copyConfigFile("log4j.dtd");
	  for (Component comp : Component.values())
	  {
		copyConfigFile(comp.getFileName(ConfigurationType.log4j));
	  }
	}
	catch (final IOException e)
	{
	  e.printStackTrace();
	}
  }

  private void copyConfigFile(final String fileName) throws IOException
  {
	try
	{
	  final OutputStream os = getOutputStream(fileName, true);
	  try
	  {
		final InputStream is = StoreManager.class.getResourceAsStream(fileName);
		if (is == null)
		{
		  System.err.println("Could not find initial resource " + fileName);
		}
		else
		{
		  try
		  {
			IOUtils.copy(is, os);
		  }
		  finally
		  {
			is.close();
		  }
		}
	  }
	  finally
	  {
		os.close();
	  }
	}
	catch (final FileAlreadyExistsException e)
	{
	  System.out.println(e.getMessage());
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

  private class FileAlreadyExistsException extends IOException
  {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	public FileAlreadyExistsException(final File file)
	{
	  super("File " + file.getAbsolutePath() + " already exists");
	}

  }

}
