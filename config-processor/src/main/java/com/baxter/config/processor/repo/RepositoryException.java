/**
 * 
 */
package com.baxter.config.processor.repo;

import com.baxter.config.processor.ProcessorException;

/**
 * @author xpdev
 * @since ${developmentVersion}
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
