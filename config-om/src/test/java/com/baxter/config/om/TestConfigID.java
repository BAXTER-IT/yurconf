/**
 * 
 */
package com.baxter.config.om;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author ykryshchuk
 * 
 */
public class TestConfigID
{

  @Test
  public void testFromURLPath_valid_3parts()
  {
	final ConfigID cid = ConfigID.fromURLPath("/com.baxter.pe/price-engine-dbserber/log4j");
	assertEquals("com.baxter.pe", cid.getProductId());
	assertEquals("price-engine-dbserber", cid.getComponentId());
	assertTrue(cid.getVariants().isEmpty());
	assertEquals("log4j", cid.getType());
  }

  @Test
  public void testFromURLPath_valid_4parts()
  {
	final ConfigID cid = ConfigID.fromURLPath("/com.baxter.pe/price-engine-broadcast/sydney/log4j");
	assertEquals("com.baxter.pe", cid.getProductId());
	assertEquals("price-engine-broadcast", cid.getComponentId());
	assertEquals(1, cid.getVariants().size());
	assertEquals("sydney", cid.getVariants().get(0));
	assertEquals("log4j", cid.getType());
  }

  @Test
  public void testFromURLPath_valid_4parts_mulVariants()
  {
	final ConfigID cid = ConfigID.fromURLPath("/com.baxter.pe/price-engine-broadcast/sydney,ha/log4j");
	assertEquals("com.baxter.pe", cid.getProductId());
	assertEquals("price-engine-broadcast", cid.getComponentId());
	assertEquals(2, cid.getVariants().size());
	assertEquals("sydney", cid.getVariants().get(0));
	assertEquals("ha", cid.getVariants().get(1));
	assertEquals("log4j", cid.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromURLPath_invalid_short()
  {
	ConfigID.fromURLPath("/com.baxter.pe/price-engine-broadcast");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromURLPath_invalid_long()
  {
	ConfigID.fromURLPath("/com.baxter.pe/price-engine-broadcast/a/b/c/d");
  }

  @Test
  public void testToURLPath_noVariants()
  {
	final ConfigID cid = new ConfigID("prod", "comp", "t1");
	assertEquals("/prod/comp/t1", cid.toURLPath());
  }

  @Test
  public void testToURLPath_1Variant()
  {
	final ConfigID cid = new ConfigID("prod", "comp", "t2", "test");
	assertEquals("/prod/comp/test/t2", cid.toURLPath());
  }

  @Test
  public void testToURLPath_3Variants()
  {
	final ConfigID cid = new ConfigID("prod", "comp", "t3", "extra", "test", "dummy");
	assertEquals("/prod/comp/extra,test,dummy/t3", cid.toURLPath());
  }

}
