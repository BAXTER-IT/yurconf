/*
 * Baxter Configuration Server
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.servlet;

import java.io.File;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baxter.config.processor.ProcessorException;
import com.baxter.config.processor.ProcessorFactory;
import com.baxter.config.processor.repo.file.ProcessorFactoryImpl;

/**
 * @author yura
 * @since 1.5
 */
public class ProcessorFactoryInitializer implements ServletContextListener
{

  private static final String REPOSITORY = "com.baxter.config.Repository";

  private static final String PROCESSOR_FACTORY = ProcessorFactory.class.getName();

  private static final String DEFAULT_REPO_PATH = System.getProperty("user.home") + File.separator
	  + ".baxter-configuration-repository";

  private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();

  /**
   * Logger instance.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorFactoryInitializer.class);

  @Override
  public void contextInitialized(final ServletContextEvent event)
  {
	LOGGER.debug("Initializing ProcessorFactory");
	final ServletContext servletContext = event.getServletContext();
	LOCK.writeLock().lock();
	try
	{
	  final String repositorySystemProperty = System.getProperty(REPOSITORY);
	  LOGGER.debug("System Property {} = {}", REPOSITORY, repositorySystemProperty);
	  if (repositorySystemProperty == null)
	  {
		final String repositoryContextParam = servletContext.getInitParameter(REPOSITORY);
		LOGGER.debug("Context Parameter {} = {}", REPOSITORY, repositoryContextParam);
		if (repositoryContextParam == null)
		{
		  registerProcessorFactory(servletContext, DEFAULT_REPO_PATH);
		}
		else
		{
		  registerProcessorFactory(servletContext, repositoryContextParam);
		}
	  }
	  else
	  {
		registerProcessorFactory(servletContext, repositorySystemProperty);
	  }
	}
	catch (final ProcessorException e)
	{
	  LOGGER.error("Could not initialize the Processor Factory", e);
	  servletContext.removeAttribute(PROCESSOR_FACTORY);
	}
	finally
	{
	  LOCK.writeLock().unlock();
	}
  }

  private void registerProcessorFactory(final ServletContext context, final String repositoryRootPath) throws ProcessorException
  {
	final File repositoryRoot = new File(repositoryRootPath);
	if (repositoryRoot.isDirectory())
	{
	  LOGGER.debug("Repository root path {}", repositoryRoot);
	}
	else
	{
	  if (repositoryRoot.mkdirs())
	  {
		LOGGER.info("Created directory {} for repository", repositoryRoot);
	  }
	  else
	  {
		LOGGER.warn("Failed to create directory {} for repository", repositoryRoot);
		throw new ProcessorException("Could not create repository directory");
	  }
	}
	context.setAttribute(PROCESSOR_FACTORY, ProcessorFactoryImpl.getInstance(repositoryRoot));
	LOGGER.info("Registered ProcessorFactory in context");
  }

  @Override
  public void contextDestroyed(final ServletContextEvent event)
  {
	// TODO Auto-generated method stub

  }

}
