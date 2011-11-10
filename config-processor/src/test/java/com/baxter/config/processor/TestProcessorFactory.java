/**
 * 
 */
package com.baxter.config.processor;

import java.io.File;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for ProcessorFactory.
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class TestProcessorFactory
{
  @Test
  public void testGetProductDirectory() {
	final File f = new File(".");
	final File pd = new File( f, "a/b/c");
	final String path = pd.getAbsolutePath();
	assertNotNull(path);
  }
}
