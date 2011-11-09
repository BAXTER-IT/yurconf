/**
 * 
 */
package com.baxter.config.om;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Configuration identifier. According to the Configuration Protocol the URL for configuration item must match the pattern
 * described in {@link http://qa/products/configuration/config-server/protocol.html#Configuration_Items}.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class ConfigID
{

  private static final Pattern URL_PATH_PATTERN = Pattern.compile("/([^/]+)/([^/]+)/([^/]+)(/([^/]+))?");

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
	final Matcher m = URL_PATH_PATTERN.matcher(urlPath);
	if (m.matches())
	{
	  final String lastGroup = m.group(5);
	  if (lastGroup == null)
	  {
		return new ConfigID(m.group(1), m.group(2), m.group(3));
	  }
	  else
	  {
		return new ConfigID(m.group(1), m.group(2), m.group(3), lastGroup);
	  }
	}
	throw new IllegalArgumentException("Invalid configuration path");
  }

  public ConfigID(final String productId, final String componentId, final String type)
  {
	this(productId, componentId, null, type);
  }

  public ConfigID(final String productId, final String componentId, final String variant, final String type)
  {
	if (productId == null || productId.isEmpty())
	{
	  throw new IllegalArgumentException("Invalid productId");
	}
	this.productId = productId;
	if (componentId == null || componentId.isEmpty())
	{
	  throw new IllegalArgumentException("Invalid componentId");
	}
	this.componentId = componentId;
	if (variant != null && variant.isEmpty())
	{
	  throw new IllegalArgumentException("Invalid variant");
	}
	this.variant = variant;
	if (type == null || type.isEmpty())
	{
	  throw new IllegalArgumentException("Invalid type");
	}
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
