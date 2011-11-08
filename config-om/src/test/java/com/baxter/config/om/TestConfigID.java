/**
 * 
 */
package com.baxter.config.om;

import org.junit.Test;

/**
 * @author ykryshchuk
 *
 */
public class TestConfigID
{

  @Test
  public void testFromURLPath_valid_3parts() {
	final ConfigID cid = ConfigID.fromURLPath("/com.baxter.pe/price-engine-dbserber/log4j");
	// TODO check parsed parts
  }
  
  @Test
  public void testFromURLPath_valid_4parts() {
	final ConfigID cid = ConfigID.fromURLPath("/com.baxter.pe/price-engine-broadcast/sydney/log4j");
	// TODO check parsed parts
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testFromURLPath_invalid_short() {
	final ConfigID cid = ConfigID.fromURLPath("/com.baxter.pe/price-engine-broadcast");
	// TODO check parsed parts
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testFromURLPath_invalid_long() {
	final ConfigID cid = ConfigID.fromURLPath("/com.baxter.pe/price-engine-broadcast/a/b/c/d");
	// TODO check parsed parts
  }
  
}
