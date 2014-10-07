/**
 *
 */
package org.yurconf.processor.desc;

/**
 * @author yura
 * @sinceDevelopmentVersion
 */
public class DescriptorException extends Exception
{

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public DescriptorException(final Throwable cause)
  {
	super(cause);
  }

  public DescriptorException(final String message, final Throwable cause)
  {
	super(message, cause);
  }

  public DescriptorException(final String message)
  {
	super(message);
  }

}
