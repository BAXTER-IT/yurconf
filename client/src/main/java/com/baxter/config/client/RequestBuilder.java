/*
 * Baxter Configuration Client
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.client;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.yurconf.om.ConfigID;
import org.yurconf.om.ConfigParameter;
import org.yurconf.om.Version;

/**
 * Configuration request builder.
 *
 * @author ykryshchuk
 * @since 1.5
 */
public final class RequestBuilder
{

  private String productId;

  private String componentId;

  private String type;

  private final List<String> preVariants = new ArrayList<String>();

  private final List<String> variants = new ArrayList<String>();

  private final List<ConfigParameter> parameters = new ArrayList<ConfigParameter>();

  private Version version;

  private boolean skipDefaultVariants;

  public RequestBuilder()
  {
  }

  private RequestBuilder(final RequestBuilder template)
  {
	this.productId = template.productId;
	this.componentId = template.componentId;
	this.type = template.type;
	this.preVariants.addAll(template.preVariants);
	this.variants.addAll(template.variants);
	this.parameters.addAll(template.parameters);
	this.version = template.version;
  }

  public RequestBuilder forType(final String type)
  {
	final RequestBuilder b = new RequestBuilder(this);
	b.type = type;
	return b;
  }

  public RequestBuilder forProduct(final String productId)
  {
	final RequestBuilder b = new RequestBuilder(this);
	b.productId = productId;
	return b;
  }

  public RequestBuilder forComponent(final String componentId)
  {
	final RequestBuilder b = new RequestBuilder(this);
	b.componentId = componentId;
	return b;
  }

  public RequestBuilder forVersion(final Version version)
  {
	final RequestBuilder b = new RequestBuilder(this);
	b.version = version;
	return b;
  }

  /**
   * Adds the specified variant in front of all other variants.
   * @param variant new variant
   * @return the builder
   */
  public RequestBuilder prependVariant( final String variant ) {
	final RequestBuilder b = new RequestBuilder(this);
	b.preVariants.add(0, variant);
	return b;
  }

  public RequestBuilder addVariant(final String variant)
  {
	final RequestBuilder b = new RequestBuilder(this);
	b.variants.add(variant);
	return b;
  }

  public RequestBuilder addVariants(final String... variants)
  {
	final RequestBuilder b = new RequestBuilder(this);
	for (String variant : variants)
	{
	  b.variants.add(variant);
	}
	return b;
  }

  public RequestBuilder addParameter(final String name, final String value)
  {
	final RequestBuilder b = new RequestBuilder(this);
	b.parameters.add(new ConfigParameter(name, value));
	return b;
  }

  public RequestBuilder addParameter(final ConfigParameter parameter)
  {
	final RequestBuilder b = new RequestBuilder(this);
	b.parameters.add(parameter);
	return b;
  }

  public Request createRequest(final Environment environment) throws MalformedURLException
  {
	final String rProductId = this.productId == null ? environment.getProductId() : this.productId;
	final String rComponentId = this.componentId == null ? environment.getComponentId() : this.componentId;
	final String rType = this.type;
	final List<String> rVariants = new ArrayList<String>(preVariants);
	if (!this.skipDefaultVariants)
	{
	  rVariants.addAll(environment.getVariants());
	}
	rVariants.addAll(this.variants);
	final ConfigID rConfigId = new ConfigID(rProductId, rComponentId, rType, rVariants);
	final Version rVersion = this.version == null ? environment.getVersion() : this.version;
	final List<ConfigParameter> rParameters = this.parameters;
	return new Request(environment.getRestUrl(), rConfigId, rVersion, rParameters);
  }

}
