/**
 * 
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

import com.baxter.config.processor.desc.Descriptor;
import com.baxter.config.processor.desc.FilenameProvider;
import com.google.common.io.Files;

/**
 * @author xpdev
 * @since ${developmentVersion}
 */
public class TestAbstractFileCommand
{

  protected File pseudoRoot;

  protected UpgradeContext upgradeContext;
  
  @Before
  public void setupEnv() {
	this.pseudoRoot = Files.createTempDir();
	final Descriptor descriptor = mock(Descriptor.class);
	when(descriptor.getSourceUrl()).thenReturn(getClass().getResource("config/default/"));
	this.upgradeContext = mock(UpgradeContext.class);
	when(this.upgradeContext.getProcessorRepositoryRoot()).thenReturn(pseudoRoot);
	when(this.upgradeContext.getDescriptor()).thenReturn(descriptor);
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
	final URL source = getClass().getResource("/com/baxter/config/processor/upgrade/config/default/" + resourceName);
	FileUtils.copyURLToFile(source, targetFile);
  }

}
