/**
 * 
 */
package com.baxter.config.processor;

import com.baxter.config.processor.desc.Descriptor;

/**
 * Abstract implementation of XSLT Processor.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public abstract class AbstractXSLTProcessor extends AbstractProcessor
{

  private static final String PARAM_XSL = "xsl";

  /**
   * Path to a stylesheet.
   */
  private String stylesheet;

  /**
   * Initializes processor.
   * 
   * @param descriptor
   *          configuration processor descriptor
   */
  protected AbstractXSLTProcessor(final Descriptor descriptor)
  {
	super(descriptor);
  }

  @Override
  protected void setParameter(final String name, final String value)
  {
	if (PARAM_XSL.equals(name))
	{
	  this.stylesheet = value;
	}
	else
	{
	  super.setParameter(name, value);
	}
  }

}
