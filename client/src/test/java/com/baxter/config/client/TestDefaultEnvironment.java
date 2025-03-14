/**
 * 
 */
package com.baxter.config.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.baxter.config.om.Version;

/**
 * @author ykryshchuk
 * 
 */
class TestDefaultEnvironment
{

  @Test
  void checkSingleton()
  {
	final Environment env1 = DefaultEnvironment.getInstance();
	final Environment env2 = DefaultEnvironment.getInstance();
	assertSame(env1, env2);
	assertEquals(DefaultEnvironment.class, env1.getClass());
  }

  @Test
  void checkDefaults()
  {
	final DefaultEnvironment env = DefaultEnvironment.getInstance();
	assertEquals("com.baxter.config", env.getDefaultProductId());
	assertEquals("testClient", env.getDefaultComponentId());
	assertEquals(Version.valueOf("1.0"), env.getDefaultVersion());
	assertEquals(Arrays.asList("demo", "test"), env.getDefaultVariants());
  }

  @Test
  void checkOverwriteProduct()
  {
	final DefaultEnvironment env = DefaultEnvironment.getInstance();
	final ConfigPropertyReplacer backup = new ConfigPropertyReplacer(DefaultEnvironment.PROP_PRODUCT_ID, "otherProduct");
	try
	{
	  assertEquals("otherProduct", env.getProductId());
	}
	finally
	{
	  backup.restore();
	}
  }

  @Test
  void checkOverwriteComponent()
  {
	final DefaultEnvironment env = DefaultEnvironment.getInstance();
	final ConfigPropertyReplacer backup = new ConfigPropertyReplacer(DefaultEnvironment.PROP_COMPONENT_ID, "otherComponent");
	try
	{
	  assertEquals("otherComponent", env.getComponentId());
	}
	finally
	{
	  backup.restore();
	}
  }

  @Test
  void checkOverwriteVersion()
  {
	final DefaultEnvironment env = DefaultEnvironment.getInstance();
	final ConfigPropertyReplacer backup = new ConfigPropertyReplacer(DefaultEnvironment.PROP_VERSION, "2.0");
	try
	{
	  assertEquals(Version.valueOf("2.0"), env.getVersion());
	}
	finally
	{
	  backup.restore();
	}
  }

  @Test
  void checkOverwriteVariants()
  {
	final DefaultEnvironment env = DefaultEnvironment.getInstance();
	final ConfigPropertyReplacer backup = new ConfigPropertyReplacer(DefaultEnvironment.PROP_VARIANTS, "just,check");
	try
	{
	  assertEquals(Arrays.asList("just", "check"), env.getVariants());
	}
	finally
	{
	  backup.restore();
	}
  }

}
