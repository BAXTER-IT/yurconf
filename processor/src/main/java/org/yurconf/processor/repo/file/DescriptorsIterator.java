/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 *
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package org.yurconf.processor.repo.file;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

import org.yurconf.processor.ProcessorException;
import org.yurconf.processor.desc.Descriptor;
import org.yurconf.processor.desc.Loader;

/**
 * @author yura
 * @since 1.5
 */
class DescriptorsIterator implements Iterator<Descriptor>
{

  /**
   * Name of the processor descriptor resource.
   */
  private static final String DESCRIPTOR_RESOURCE = "META-INF/yurconf.processor.xml";

  private final Enumeration<URL> descriptorResources;

  private URL lastDescriptorResource;

  DescriptorsIterator() throws IOException
  {
	this.descriptorResources = Thread.currentThread().getContextClassLoader().getResources(DESCRIPTOR_RESOURCE);
  }

  @Override
  public boolean hasNext()
  {
	return descriptorResources.hasMoreElements();
  }

  @Override
  public Descriptor next()
  {
	this.lastDescriptorResource = descriptorResources.nextElement();
	try
	{
	  return Loader.getInstance().load(lastDescriptorResource);
	}
	catch (final ProcessorException e)
	{
	  throw new IllegalStateException("Could not load descriptor for " + lastDescriptorResource, e);
	}
  }

  URL getLastDescriptorResource()
  {
	return lastDescriptorResource;
  }

  @Override
  public void remove()
  {
	throw new UnsupportedOperationException();
  }

}
