/**
 * 
 */
package com.baxter.config.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.baxter.config.om.Version;

/**
 * Default implementation of configuration environment. The values are provided in a resource file, but these settings can be
 * overwritten with system properties.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 * 
 */
class DefaultEnvironment implements Environment
{

  static final String PREFIX = "com.baxter.config.";

  static final String PROP_REST_URL = "restUrl";
  static final String PROP_PRODUCT_ID = "productId";
  static final String PROP_COMPONENT_ID = "componentId";
  static final String PROP_VERSION = "version";
  static final String PROP_VARIANTS = "variants";

  private static final String DEFAULT_REST_URL = "http://localhost:8080/baxter-config/rest/";

  private static final String CONFIG_RESOURCE = "META-INF/services/com.baxter.config.properties";

  private static final DefaultEnvironment INSTANCE = new DefaultEnvironment();

  private final URL restUrl;

  private final String defaultProductId;
  private final String defaultComponentId;
  private final Version defaultVersion;
  private final List<String> defaultVariants;

  private DefaultEnvironment()
  {
	this.restUrl = detectRestUrl();
	final Properties defaultProperties = loadDefaultsFromResource();
	this.defaultProductId = defaultProperties.getProperty(PROP_PRODUCT_ID);
	this.defaultComponentId = defaultProperties.getProperty(PROP_COMPONENT_ID);
	this.defaultVersion = Version.valueOf(defaultProperties.getProperty(PROP_VERSION));
	final String defaultVariantsLine = defaultProperties.getProperty(PROP_VARIANTS);
	this.defaultVariants = Arrays.asList(defaultVariantsLine.split(","));
  }

  private static Properties loadDefaultsFromResource()
  {
	final Properties properties = new Properties();
	final URL propertiesResource = Thread.currentThread().getContextClassLoader().getResource(CONFIG_RESOURCE);
	if (propertiesResource != null)
	{
	  try
	  {
		final InputStream stream = propertiesResource.openStream();
		try
		{
		  properties.load(stream);
		}
		finally
		{
		  stream.close();
		}
	  }
	  catch (final IOException e)
	  {
		// Failed to load the configuration file
	  }
	}
	return properties;
  }

  private static URL detectRestUrl()
  {
	final String restUrlString = System.getProperty(PREFIX + PROP_REST_URL);
	if (restUrlString != null)
	{
	  try
	  {
		return new URL(restUrlString);
	  }
	  catch (final MalformedURLException e)
	  {
		throw new IllegalStateException("Cannot build Rest URL", e);
	  }
	}
	try
	{
	  return new URL(DEFAULT_REST_URL);
	}
	catch (final MalformedURLException e2)
	{
	  throw new IllegalStateException("Cannot build default Rest URL", e2);
	}
  }

  static DefaultEnvironment getInstance()
  {
	return INSTANCE;
  }

  @Override
  public URL getRestUrl()
  {
	return this.restUrl;
  }

  @Override
  public String getProductId()
  {
	return System.getProperty(PREFIX + PROP_PRODUCT_ID, this.defaultProductId);
  }

  @Override
  public String getComponentId()
  {
	return System.getProperty(PREFIX + PROP_COMPONENT_ID, this.defaultComponentId);
  }

  @Override
  public List<String> getVariants()
  {
	final String variantsLine = System.getProperty(PREFIX + PROP_VARIANTS);
	return (variantsLine == null || variantsLine.isEmpty()) ? this.defaultVariants : Arrays.asList(variantsLine.split(","));
  }

  @Override
  public Version getVersion()
  {
	final String versionLine = System.getProperty(PREFIX + PROP_VERSION);
	return versionLine == null ? this.defaultVersion : Version.valueOf(versionLine);
  }

  String getDefaultProductId()
  {
	return defaultProductId;
  }

  String getDefaultComponentId()
  {
	return defaultComponentId;
  }

  Version getDefaultVersion()
  {
	return defaultVersion;
  }

  List<String> getDefaultVariants()
  {
	return defaultVariants;
  }

}
