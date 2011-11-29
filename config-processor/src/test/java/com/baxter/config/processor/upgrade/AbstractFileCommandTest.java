/**
 * 
 */
package com.baxter.config.processor.upgrade;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;

import com.baxter.config.processor.desc.Descriptor;
import com.google.common.io.Files;

/**
 * @author xpdev
 * @since ${developmentVersion}
 */
public abstract class AbstractFileCommandTest
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

}
