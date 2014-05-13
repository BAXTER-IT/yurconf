/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.processor.repo;

import com.baxter.config.processor.ProcessorException;

/**
 * @author xpdev
 * @since 1.5
 */
public class RepositoryException extends ProcessorException
{

  /**
   * Serialization identifier.
   */
  private static final long serialVersionUID = 1L;

  public RepositoryException(final Throwable cause)
  {
	super(cause);
  }

  public RepositoryException(final String message)
  {
	super(message);
  }
}
