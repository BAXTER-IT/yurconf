/**
 * 
 */
package com.baxter.config.processor;

import com.baxter.config.processor.desc.Descriptor;

/**
 * Default implementation of Configuration XSLT Processor.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class XSLTProcessor extends AbstractXSLTProcessor
{

  /**
   * Creates processor for specified descriptor.
   * 
   * @param descriptor
   *          processor descriptor
   */
  public XSLTProcessor(final Descriptor descriptor)
  {
	super(descriptor);
  }

  @Override
  public void process(final ProcessorContext context) throws ProcessorException
  {
	// TODO Auto-generated method stub

  }

}
