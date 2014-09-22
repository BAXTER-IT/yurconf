/*
 * Yurconf Client
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.junit.Test;
import org.yurconf.client.DefaultEnvironment;
import org.yurconf.client.Environment;
import org.yurconf.om.Version;

/**
 * @author ykryshchuk
 * 
 */
public class TestDefaultEnvironment
{

  @Test
  public void checkSingleton()
  {
	final Environment env1 = DefaultEnvironment.getInstance();
	final Environment env2 = DefaultEnvironment.getInstance();
	assertSame(env1, env2);
	assertEquals(DefaultEnvironment.class, env1.getClass());
  }

  @Test
  public void checkDefaults()
  {
	final DefaultEnvironment env = DefaultEnvironment.getInstance();
	assertEquals("com.baxter.config", env.getDefaultProductId());
	assertEquals("testClient", env.getDefaultComponentId());
	assertEquals(Version.valueOf("1.0"), env.getDefaultVersion());
	assertEquals(Arrays.asList("demo", "test"), env.getDefaultVariants());
  }

  @Test
  public void checkOverwriteProduct()
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
  public void checkOverwriteComponent()
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
  public void checkOverwriteVersion()
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
  public void checkOverwriteVariants()
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
