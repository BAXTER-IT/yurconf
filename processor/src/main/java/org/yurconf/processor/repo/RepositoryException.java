/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.processor.repo;

import org.yurconf.processor.ProcessorException;

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
