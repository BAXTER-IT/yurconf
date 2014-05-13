/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.processor;

/**
 * Base class for processor exceptions.
 *
 * @author ykryshchuk
 * @since 1.5
 */
public class ProcessorException extends Exception
{

  /**
   * Serialization identifier.
   */
  private static final long serialVersionUID = 1L;

  /** {@inheritDoc} */
  public ProcessorException(final String message)
  {
	super(message);
  }

  /** {@inheritDoc} */
  public ProcessorException(final Throwable cause)
  {
	super(cause);
  }

  /** {@inheritDoc} */
  public ProcessorException(final String message, final Throwable cause)
  {
	super(message, cause);
  }

}
