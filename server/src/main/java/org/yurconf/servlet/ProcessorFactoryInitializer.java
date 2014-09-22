/*
 * Yurconf Server
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.servlet;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yurconf.processor.ProcessorException;
import org.yurconf.processor.ProcessorFactory;
import org.yurconf.processor.repo.file.ProcessorFactoryImpl;

/**
 * @author yura
 * @since 1.5
 */
public class ProcessorFactoryInitializer implements ServletContextListener
{

  private static final String REPOSITORY = "yurconf.repository";

  private static final String PROCESSOR_FACTORY = ProcessorFactory.class.getName();

  private static final Path DEFAULT_REPO_PATH = FileSystems.getDefault().getPath(
  // By default the repository is located under user's home
	  System.getProperty("user.home"), ".yurconf", "repository");

  private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();

  /**
   * Logger instance.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorFactoryInitializer.class);

  private final ClassLoader processorsCL;

  public ProcessorFactoryInitializer(final ClassLoader processorsCL)
  {
	this.processorsCL = processorsCL;
  }

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
		  registerProcessorFactory(servletContext, FileSystems.getDefault().getPath(repositoryContextParam));
		}
	  }
	  else
	  {
		registerProcessorFactory(servletContext, FileSystems.getDefault().getPath(repositorySystemProperty));
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

  private void registerProcessorFactory(final ServletContext context, final Path repositoryRootPath) throws ProcessorException
  {
	final File repositoryRoot = repositoryRootPath.toFile();
	if (repositoryRoot.isDirectory())
	{
	  LOGGER.info("Repository root path {}", repositoryRoot);
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
	context.setAttribute(PROCESSOR_FACTORY, ProcessorFactoryImpl.getInstance(repositoryRoot,processorsCL));
	LOGGER.info("Registered ProcessorFactory in context");
  }

  @Override
  public void contextDestroyed(final ServletContextEvent event)
  {
	// TODO Auto-generated method stub

  }

}
