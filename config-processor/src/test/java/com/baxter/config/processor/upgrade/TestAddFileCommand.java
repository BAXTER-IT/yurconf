/**
 * 
 */
package com.baxter.config.processor.upgrade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;

import com.baxter.config.processor.desc.FilenameProvider;

/**
 * Test cases for AddFileCommand.
 * 
 * @author xpdev
 * @since ${developmentVersion}
 */
public class TestAddFileCommand extends AbstractFileCommandTest
{
  
  @Test
  public void check_isFilenamePatternEffective()
  {
	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNameMask()).thenReturn("somedir/somefile.ext");
	when(fnProvider.getFileNamePattern()).thenReturn("[^\\.]*\\.[^\\.]*");
	final AddFileCommand cmd = new AddFileCommand(fnProvider);
	assertTrue(cmd.isFilenamePatternEffective());
  }

  /**
   * 
   */
  @Test
  public void check_addNewFileSingle() throws Exception
  {
	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNameMask()).thenReturn("somedir/somefile.ext");

	final AddFileCommand cmd = new AddFileCommand(fnProvider);

	final File expectedNewFile = new File(this.pseudoRoot, "somedir/somefile.ext");
	assertFalse(expectedNewFile.isFile());
	cmd.upgrade(this.upgradeContext);
	assertTrue(expectedNewFile.isFile());
	assertEquals(10, expectedNewFile.length());
  }

  /**
   * 
   */
  @Test
  public void check_addNewFileMask() throws Exception
  {
	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNameMask()).thenReturn("somedir/somefile.*");
	final AddFileCommand cmd = new AddFileCommand(fnProvider);
	final File expectedNewFile = new File(this.pseudoRoot, "somedir/somefile.ext");
	assertFalse(expectedNewFile.isFile());
	cmd.upgrade(this.upgradeContext);
	assertTrue(expectedNewFile.isFile());
	assertEquals(10, expectedNewFile.length());
  }

  /**
   * 
   */
  @Test
  public void check_addNewFileMask2() throws Exception
  {
	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNameMask()).thenReturn("*/*.ext");
	final AddFileCommand cmd = new AddFileCommand(fnProvider);
	assertFalse(new File(this.pseudoRoot, "somedir/somefile.ext").isFile());
	assertFalse(new File(this.pseudoRoot, "otherdir/otherfile.ext").isFile());
	cmd.upgrade(this.upgradeContext);
	assertTrue(new File(this.pseudoRoot, "somedir/somefile.ext").isFile());
	assertTrue(new File(this.pseudoRoot, "otherdir/otherfile.ext").isFile());
  }

  /**
   * 
   */
  @Test
  public void check_addNewFileRegexp() throws Exception
  {
	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNamePattern()).thenReturn("input1_[1-3]\\.txt");
	final AddFileCommand cmd = new AddFileCommand(fnProvider);
	for (int i = 1; i <= 3; i++)
	{
	  assertFalse(new File(this.pseudoRoot, "input1_" + i + ".txt").isFile());
	}
	cmd.upgrade(this.upgradeContext);
	for (int i = 1; i <= 3; i++)
	{
	  assertTrue(new File(this.pseudoRoot, "input1_" + i + ".txt").isFile());
	}
  }

}
