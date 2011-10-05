package com.baxter.config.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.CopyUtils;

/**
 * Application Lifecycle Listener implementation class StoreManagerInit
 *
 */
@SuppressWarnings("deprecation")
public class StoreManagerInit implements ServletContextListener
{
  private static final String BAXTER_CONFIG_DIR = "/.baxter-config";
  private final String USER_HOME = System.getProperty("user.home");

  /**
   * Default constructor. 
   */
  public StoreManagerInit()
  {
	// TODO Auto-generated constructor stub
  }

  /**
   * @see ServletContextListener#contextInitialized(ServletContextEvent)
   */
  public void contextInitialized(ServletContextEvent sce)
  {
	sce.getServletContext().setAttribute("storeManager", new StoreManager());
	try
	{
	  copyFileStructure(BAXTER_CONFIG_DIR);
	}
	catch (IOException e)
	{
	  e.printStackTrace();
	}

  }

  /**
   * @see ServletContextListener#contextDestroyed(ServletContextEvent)
   */
  public void contextDestroyed(ServletContextEvent sce)
  {
	// TODO Auto-generated method stub
  }

  private void copyFileStructure(String path) throws IOException
  {
	final File destination = new File(USER_HOME, path);
	final URL url = StoreManagerInit.class.getResource(path);
	final File source = new File(url.getFile());
	if (!destination.exists() && source.isDirectory())
	{
	  destination.mkdir();
	}

	if (destination.exists() && source.isDirectory())
	{
	  for (String subPath : source.list())
	  {
		copyFileStructure(path + "/" + subPath);
	  }
	  return;
	}

	if (!destination.exists() && source.isFile())
	{
	  destination.createNewFile();
	  CopyUtils.copy(StoreManagerInit.class.getResourceAsStream(path), new FileOutputStream(destination));
	}

  }
}
