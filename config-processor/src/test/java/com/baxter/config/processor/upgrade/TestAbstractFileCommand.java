/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.processor.upgrade;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.baxter.config.processor.ConfigurationRepository;
import com.baxter.config.processor.ProcessorFactory;
import com.baxter.config.processor.desc.Descriptor;
import com.baxter.config.processor.desc.FilenameProvider;
import com.google.common.io.Files;

/**
 * @author xpdev
 * @since 1.5
 */
public class TestAbstractFileCommand
{

  private static final String TEST_PRODUCT_ID = "test.product";

  protected File pseudoRoot;

  protected UpgradeContext upgradeContext;

  @Before
  public void setupEnv() {
	this.pseudoRoot = Files.createTempDir();

	final Descriptor descriptor = mock(Descriptor.class);
	when(descriptor.getSourceUrl()).thenReturn(getClass().getResource("config/default/"));
	when(descriptor.getXslUrl()).thenReturn(getClass().getResource("config/xsl/"));
	when(descriptor.getProductId()).thenReturn(TEST_PRODUCT_ID);

	final ConfigurationRepository repository = mock(ConfigurationRepository.class);
	when( repository.getProductDirectory(TEST_PRODUCT_ID)).thenReturn(this.pseudoRoot);

	final ProcessorFactory processorFactory = mock(ProcessorFactory.class);
	when( processorFactory.getRepository() ).thenReturn( repository );

	this.upgradeContext = mock(UpgradeContext.class);
	when(this.upgradeContext.getDescriptor()).thenReturn(descriptor);
	when(this.upgradeContext.getProcessorFactory()).thenReturn(processorFactory);

  }

  @Test
  public void check_isFilenamePatternEffective()
  {
	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNameMask()).thenReturn("somedir/somefile.ext");
	when(fnProvider.getFileNamePattern()).thenReturn("[^\\.]*\\.[^\\.]*");
	final AbstractFileCommand cmd = new AbstractFileCommand(fnProvider){

	};
	assertTrue(cmd.isFilenamePatternEffective());
  }

  /**
   *
   * @param resourceName will be resolved relative to config/default from resources
   */
  protected void installFileToRoot( final String resourceName ) throws IOException {

	final File targetFile = new File( this.pseudoRoot, resourceName );
	final URL source = getClass().getResource("config/default/" + resourceName);
	FileUtils.copyURLToFile(source, targetFile);
  }

}
