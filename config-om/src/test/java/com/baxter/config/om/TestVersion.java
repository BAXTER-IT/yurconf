/**
 * 
 */
package com.baxter.config.om;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test for {@link Version} class.
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class TestVersion
{

  @Test
  public void testValueOf_1_0() {
	final Version v = Version.valueOf("1.0");
	assertEquals(2, v.getParts().length);
	assertEquals(1, v.getParts()[0]);
	assertEquals(0, v.getParts()[1]);
  }
  
  @Test
  public void testValueOf_1_5_3() {
	final Version v = Version.valueOf("1.5.3");
	assertEquals(3, v.getParts().length);
	assertEquals(1, v.getParts()[0]);
	assertEquals(5, v.getParts()[1]);
	assertEquals(3, v.getParts()[2]);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testValueOf_1_a() {
	Version.valueOf("1.a");
  }
  
  @Test
  public void testValueOfToString_1_1_0() {
	final Version v=  Version.valueOf("1.1.0");
	assertEquals("1.1.0", v.toString());
  }
  
}
