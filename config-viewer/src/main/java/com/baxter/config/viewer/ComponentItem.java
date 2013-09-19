/**
 * 
 */
package com.baxter.config.viewer;

/**
 * @author yura
 * @sinceDevelopmentVersion
 */
public class ComponentItem
{

  private final ProductItem product;

  private final String id;

  public ComponentItem(final ProductItem product, final String id)
  {
	this.product = product;
	this.id = id;
  }

  public ProductItem getProduct()
  {
	return product;
  }

  public String getId()
  {
	return id;
  }

}
