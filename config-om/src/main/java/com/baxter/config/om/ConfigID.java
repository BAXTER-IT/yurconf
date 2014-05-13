/*
 * Baxter Configuration Object Model
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
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
 * @since 1.5
 */
public class ConfigID
{

  private static final Pattern URL_PATH_PATTERN = Pattern.compile("/([^/]+)/([^/]+)(/([^/]+))?/([^/]+)");

  private static final int GRP_PRODUCT = 1;
  private static final int GRP_COMPONENT = 2;
  private static final int GRP_VARIANTS = 4;
  private static final int GRP_TYPE = 5;

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
	  final String vGroup = m.group(GRP_VARIANTS);
	  if (vGroup == null)
	  {
		return new ConfigID(m.group(GRP_PRODUCT), m.group(GRP_COMPONENT), m.group(GRP_TYPE));
	  }
	  else
	  {
		final String[] variants = vGroup.split(",");
		return new ConfigID(m.group(GRP_PRODUCT), m.group(GRP_COMPONENT), m.group(GRP_TYPE), variants);
	  }
	}
	throw new IllegalArgumentException("Invalid configuration path");
  }

  public ConfigID(final String productId, final String componentId, final String type, final List<String> variants)
  {
	this(productId, componentId, type, variants.toArray(new String[variants.size()]));
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

  public String toURLPath()
  {
	final StringBuilder str = new StringBuilder();
	str.append("/").append(getProductId());
	str.append("/").append(getComponentId());
	if (this.variants.length != 0)
	{
	  str.append("/");
	  for (int i = 0; i < this.variants.length; i++)
	  {
		if (i != 0)
		{
		  str.append(",");
		}
		str.append(this.variants[i]);
	  }
	}
	str.append("/").append(getType());
	return str.toString();
  }

}
