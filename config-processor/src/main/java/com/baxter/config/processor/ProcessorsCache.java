/**
 * 
 */
package com.baxter.config.processor;

import java.util.HashMap;
import java.util.Map;

import com.baxter.config.om.ConfigID;

/**
 * Processors cache. Contains all loaded processors.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
class ProcessorsCache
{

  /**
   * Map of processors. Key is a product name, value is another map where key is configuration type and value is a processor.
   */
  private final Map<String, Map<String,? extends AbstractProcessor>> byProductId = new HashMap<String, Map<String,? extends AbstractProcessor>>();
  
  /**
   * Finds the processor by productId and configuration type.
   * @param configId configuration identifier
   * @return processor or null if could not find a processor
   */
  AbstractProcessor findProcessor( final ConfigID configId ) {
	final Map<String, ? extends AbstractProcessor> byConfigurationType = byProductId.get(configId.getProductId());
	if ( byConfigurationType == null ) {
	  return null;
	} else {
	  return null;
	}
  }
  
}
