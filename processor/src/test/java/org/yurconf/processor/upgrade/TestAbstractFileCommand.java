/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.processor.upgrade;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.yurconf.processor.ConfigurationRepository;
import org.yurconf.processor.ProcessorFactory;
import org.yurconf.processor.desc.Descriptor;
import org.yurconf.processor.desc.FilenameProvider;
import org.yurconf.processor.upgrade.AbstractFileCommand;
import org.yurconf.processor.upgrade.UpgradeContext;

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
  public void setupEnv() throws Exception {
	this.pseudoRoot = Files.createTempDirectory("yurconf-test-").toFile();

	final Descriptor descriptor = mock(Descriptor.class);
	when(descriptor.getRootUri()).thenReturn(getClass().getResource("config/xsl/").toURI());
	when(descriptor.getDefaultSourceUri()).thenReturn(getClass().getResource("config/default/").toURI());
	when(descriptor.getProductId()).thenReturn(TEST_PRODUCT_ID);

	final ConfigurationRepository repository = mock(ConfigurationRepository.class);
	when( repository.getProductDirectory(TEST_PRODUCT_ID)).thenReturn(this.pseudoRoot);
	when( repository.getDescriptor(TEST_PRODUCT_ID) ).thenReturn(descriptor);

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
