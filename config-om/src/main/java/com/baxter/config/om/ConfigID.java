/**
 * 
 */
package com.baxter.config.om;

/**
 * Configuration identifier. According to the Configuration Protocol the URL for configuration item must match the pattern
 * described in {@link http://qa/products/configuration/config-server/protocol.html#Configuration_Items}.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class ConfigID
{

  private final String productId;

  private final String componentId;

  private final String type;

  private final String variant;

  /**
   * Returns the configuration identifier specified by a part of URL.
   * 
   * @param urlPath
   *          URL Path as defined by Configuration Protocol.
   * @return the parsed Configuration Identifier.
   * @throws IllegalArgumentException
   *           if cannot parse the path
   */
  public static ConfigID fromURLPath(final String urlPath)
  {
	final String[] parts = urlPath.split("/");
	switch (parts.length)
	{
	case 3:
	  return new ConfigID(parts[0], parts[1], parts[2]);
	case 4:
	  return new ConfigID(parts[0], parts[1], parts[2], parts[3]);
	default:
	  throw new IllegalArgumentException("Invalid configuration path");
	}
  }

  public ConfigID(final String productId, final String componentId, final String type)
  {
	this(productId, componentId, null, type);
  }

  public ConfigID(final String productId, final String componentId, final String variant, final String type)
  {
	this.productId = productId;
	this.componentId = componentId;
	this.variant = variant;
	this.type = type;
  }

  public String getProductId()
  {
	return productId;
  }

  public String getComponentId()
  {
	return componentId;
  }

  public String getVariant()
  {
	return variant;
  }

  public String getType()
  {
	return type;
  }

}
