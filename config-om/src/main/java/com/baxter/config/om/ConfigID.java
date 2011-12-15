/**
 * 
 */
package com.baxter.config.om;

import java.util.Arrays;
import java.util.List;
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

  private final String[] variants;

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
		final String[] variants = m.group(3).split(",");
		return new ConfigID(m.group(1), m.group(2), lastGroup, variants);
	  }
	}
	throw new IllegalArgumentException("Invalid configuration path");
  }

  public ConfigID(final String productId, final String componentId, final String type, final String... variants)
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
	if (type == null || type.isEmpty())
	{
	  throw new IllegalArgumentException("Invalid type");
	}
	this.type = type;
	this.variants = variants;
  }

  public String getProductId()
  {
	return productId;
  }

  public String getComponentId()
  {
	return componentId;
  }

  public List<String> getVariants()
  {
	return Arrays.asList(this.variants);
  }

  public String getType()
  {
	return type;
  }

}
