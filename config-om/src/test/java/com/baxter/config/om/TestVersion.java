/**
 * 
 */
package com.baxter.config.om;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link Version} class.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
class TestVersion
{

  @Test
  void testValueOf_100()
  {
	final Version v = Version.valueOf("100");
	assertEquals(1, v.getParts().length);
	assertEquals(100, v.getParts()[0]);
  }

  @Test
  void testValueOf_1_0()
  {
	final Version v = Version.valueOf("1.0");
	assertEquals(2, v.getParts().length);
	assertEquals(1, v.getParts()[0]);
	assertEquals(0, v.getParts()[1]);
  }

  @Test
  void testValueOf_1_5_3()
  {
	final Version v = Version.valueOf("1.5.3");
	assertEquals(3, v.getParts().length);
	assertEquals(1, v.getParts()[0]);
	assertEquals(5, v.getParts()[1]);
	assertEquals(3, v.getParts()[2]);
  }

  @Test
  void testValueOf_1_a()
  {
	assertThrows(IllegalArgumentException.class, () -> {
	  Version.valueOf("1.a");
	});
  }

  @Test
  void testValueOfToString_1_1_0()
  {
	final Version v = Version.valueOf("1.1.0");
	assertEquals("1.1.0", v.toString());
  }

  @Test
  void testCompare_1_5_and_1_5()
  {
	assertTrue(Version.valueOf("1.5").compareTo(Version.valueOf("1.5")) == 0);
  }

  @Test
  void testCompare_1_5_and_1_6()
  {
	assertTrue(Version.valueOf("1.5").compareTo(Version.valueOf("1.6")) < 0);
  }

  @Test
  void testCompare_1_5_and_1_0()
  {
	assertTrue(Version.valueOf("1.5").compareTo(Version.valueOf("1.0")) > 0);
  }

  @Test
  void testValueOf_null()
  {
	final Version v = Version.valueOf(null);
	assertNull(v);
  }

}
