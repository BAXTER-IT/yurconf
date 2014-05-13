/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.processor.upgrade;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.baxter.config.processor.desc.FilenameProvider;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestRemoveFileCommand extends TestAbstractFileCommand
{

  @Test
  public void check_removeFileSingle() throws IOException, UpgradeException
  {
	final File expectedFile = new File(this.pseudoRoot, "somedir/somefile.ext");
	FileUtils.touch(expectedFile);
	assertTrue(expectedFile.exists());
	
	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNameMask()).thenReturn("somedir/somefile.ext");
	final RemoveFileCommand cmd = new RemoveFileCommand(fnProvider);
	cmd.upgrade(this.upgradeContext);
	assertFalse(expectedFile.exists());
  }
  
  @Test
  public void check_removeFilesByWildCards() throws IOException, UpgradeException
  {
	installFileToRoot("input1_1.txt");
	final File expectedFile1 = new File(this.pseudoRoot, "input1_1.txt");
	installFileToRoot("input1_2.txt");
	final File expectedFile2 = new File(this.pseudoRoot, "input1_2.txt");
	installFileToRoot("input2_1.txt");
	final File expectedFile3 = new File(this.pseudoRoot, "input2_1.txt");
	assertTrue(expectedFile1.exists());
	assertTrue(expectedFile2.exists());
	assertTrue(expectedFile3.exists());
	
	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNameMask()).thenReturn("input1_*.txt");
	final RemoveFileCommand cmd = new RemoveFileCommand(fnProvider);
	cmd.upgrade(this.upgradeContext);
	assertFalse(expectedFile1.exists());
	assertFalse(expectedFile2.exists());
	assertTrue(expectedFile3.exists());
  }
  
  @Test
  public void check_removeFilesByRegex() throws IOException, UpgradeException
  {
	installFileToRoot("input1_1.txt");
	final File expectedFile1 = new File(this.pseudoRoot, "input1_1.txt");
	installFileToRoot("input1_2.txt");
	final File expectedFile2 = new File(this.pseudoRoot, "input1_2.txt");
	installFileToRoot("input2_1.txt");
	final File expectedFile3 = new File(this.pseudoRoot, "input2_1.txt");
	assertTrue(expectedFile1.exists());
	assertTrue(expectedFile2.exists());
	assertTrue(expectedFile3.exists());
	
	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNamePattern()).thenReturn("([^_]*)_1.txt");
	final RemoveFileCommand cmd = new RemoveFileCommand(fnProvider);
	cmd.upgrade(this.upgradeContext);
	assertFalse(expectedFile1.exists());
	assertTrue(expectedFile2.exists());
	assertFalse(expectedFile3.exists());
  }
  
  @Test
  //If there is nothing to delete don't need to do anything.
  public void check_removeNonExistingFile() throws IOException, UpgradeException
  {
	  final File expectedFile = new File(this.pseudoRoot, "somedir/somefile.ext");
	  assertFalse(expectedFile.exists());
	  
	  final FilenameProvider fnProvider = mock(FilenameProvider.class);
	  when(fnProvider.getFileNameMask()).thenReturn("somedir/somefile.ext");
	  final RemoveFileCommand cmd = new RemoveFileCommand(fnProvider);
	  cmd.upgrade(this.upgradeContext);
	  assertFalse(expectedFile.exists());
  }

}
