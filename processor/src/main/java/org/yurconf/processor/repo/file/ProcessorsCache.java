/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.processor.repo.file;

import java.util.HashMap;
import java.util.Map;

import org.yurconf.processor.AbstractProcessor;

import org.yurconf.om.ConfigID;

/**
 * Processors cache. Contains all loaded processors.
 *
 * @author ykryshchuk
 * @since 1.5
 */
class ProcessorsCache
{

  /**
   * Map of processors. Key is a product name, value is another map where key is configuration type and value is a processor.
   */
  private final Map<String, Map<String, AbstractProcessor>> byProductId = new HashMap<String, Map<String, AbstractProcessor>>();

  /**
   * Finds the processor by productId and configuration type.
   *
   * @param configId
   *          configuration identifier
   * @return processor or null if could not find a processor
   */
  AbstractProcessor findProcessor(final ConfigID configId)
  {
	final Map<String, AbstractProcessor> byConfigurationType = byProductId.get(configId.getProductId());
	if (byConfigurationType == null)
	{
	  return null;
	}
	else
	{
	  final AbstractProcessor processor = byConfigurationType.get(configId.getType());
	  if (processor == null)
	  {
		return byConfigurationType.get("*");
	  }
	  else
	  {
		return processor;
	  }
	}
  }

  /**
   * Registers new Processor with this cache.
   *
   * @param productId
   *          processor product id
   * @param type
   *          processor type
   * @param processor
   *          processor instance
   */
  void registerProcessor(final String productId, final String type, final AbstractProcessor processor)
  {
	if (!byProductId.containsKey(productId))
	{
	  byProductId.put(productId, new HashMap<String, AbstractProcessor>());
	}
	final Map<String, AbstractProcessor> byConfigurationType = byProductId.get(productId);
	byConfigurationType.put(type, processor);
  }

}
