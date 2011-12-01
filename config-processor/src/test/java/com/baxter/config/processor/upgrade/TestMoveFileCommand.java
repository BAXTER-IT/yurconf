/**
 * 
 */
package com.baxter.config.processor.upgrade;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.baxter.config.processor.desc.FilenameProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author xpdev
 * @since ${developmentVersion}
 */
public class TestMoveFileCommand extends TestAbstractFileCommand

{

  @Test
  public void testRenameWithRegExp()
  {
	final Pattern p = Pattern.compile("1");
	final Matcher m = p.matcher("this in 1 line with 1 sample"); //this in one line with one sample 
	final StringBuffer str = new StringBuffer();
	while (m.find())
	{
	  m.appendReplacement(str, "one");
	}
	m.appendTail(str);
	assertEquals("this in one line with one sample", str.toString());
  }

  @Test
  public void testRenameWithRegExp2()
  {
	// rename dir/* -> log2/*
	final String str1 = "dir/(.*)";
	final String str2 = "log2/$1";
	final String test1 = "dir/file1.txt";
	final String result1 = test1.replaceAll(str1, str2);
	assertEquals("log2/file1.txt", result1);
	final String test2 = "dir/subdir/file1.txt";
	final String result2 = test2.replaceAll(str1, str2);
	assertEquals("log2/subdir/file1.txt", result2);
  }

  @Test
  public void testRenameWithRegExp3()
  {
	// rename dir/* -> log2/*
	final String str1 = "dir/([^\\.]*)\\.([^\\.]*)";
	final String str2 = "log2/$1\\.xml";

	final String test1 = "dir/file1.txt";
	final String result1 = test1.replaceAll(str1, str2);
	assertEquals("log2/file1.xml", result1);

	final String test2 = "dir/subdir/file1.txt";
	final String result2 = test2.replaceAll(str1, str2);
	assertEquals("log2/subdir/file1.xml", result2);
  }

  @Test
  public void check_singleFileRename() throws IOException, UpgradeException
  {
	final File expectedFile = new File(this.pseudoRoot, "somedir/somefile.ext");
	final File renamedFile = new File(this.pseudoRoot, "somedir/somefile2.ext");
	FileUtils.touch(expectedFile);
	assertTrue(expectedFile.exists());
	assertFalse(renamedFile.exists());

	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNameMask()).thenReturn("somedir/somefile.ext");
	final MoveFileCommand cmd = new MoveFileCommand(fnProvider, "somedir/somefile2.ext");
	cmd.upgrade(this.upgradeContext);
	assertFalse(expectedFile.exists());
	assertTrue(renamedFile.exists());
  }

  @Test
  public void check_singleFileReplace() throws IOException, UpgradeException
  {
	final File expectedFile = new File(this.pseudoRoot, "somedir/somefile.ext");
	final File renamedFile = new File(this.pseudoRoot, "otherdir/somefile.ext");
	FileUtils.touch(expectedFile);
	assertTrue(expectedFile.exists());
	assertFalse(renamedFile.exists());

	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNameMask()).thenReturn("somedir/somefile.ext");
	final MoveFileCommand cmd = new MoveFileCommand(fnProvider, "otherdir/somefile.ext");
	cmd.upgrade(this.upgradeContext);
	assertFalse(expectedFile.exists());
	assertTrue(renamedFile.exists());
  }

  @Test
  public void check_singleFileReplaceRename() throws IOException, UpgradeException
  {
	final File expectedFile = new File(this.pseudoRoot, "somedir/somefile.ext");
	final File renamedFile = new File(this.pseudoRoot, "otherdir/somefile2.ext");
	FileUtils.touch(expectedFile);
	assertTrue(expectedFile.exists());
	assertFalse(renamedFile.exists());

	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNameMask()).thenReturn("somedir/somefile.ext");
	final MoveFileCommand cmd = new MoveFileCommand(fnProvider, "otherdir/somefile2.ext");
	cmd.upgrade(this.upgradeContext);
	assertFalse(expectedFile.exists());
	assertTrue(renamedFile.exists());
  }

  @Test
  public void check_singleFileReplaceToExistingOne() throws IOException, UpgradeException
  {
	final File expectedFile = new File(this.pseudoRoot, "somedir/somefile.ext");
	final File renamedFile = new File(this.pseudoRoot, "otherdir/somefile2.ext");
	installFileToRoot("somedir/somefile.ext");
	FileUtils.touch(renamedFile);
	assertTrue(expectedFile.exists());
	assertEquals(10, expectedFile.length());
	assertTrue(renamedFile.exists());
	assertEquals(0, renamedFile.length());

	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNameMask()).thenReturn("somedir/somefile.ext");
	final MoveFileCommand cmd = new MoveFileCommand(fnProvider, "otherdir/somefile2.ext");
	cmd.upgrade(this.upgradeContext);
	assertFalse(expectedFile.exists());
	assertTrue(renamedFile.exists());
	assertEquals(10, renamedFile.length());
  }
  
  @Test
  public void check_WildcardFileReplaceToDirectoryWithExistingFile() throws IOException, UpgradeException
  {
	final File expectedFile = new File(this.pseudoRoot, "somedir/somefile.ext");
	final File renamedFile = new File(this.pseudoRoot, "otherdir/somefile.ext");
	installFileToRoot("somedir/somefile.ext");
	FileUtils.touch(renamedFile);
	assertTrue(expectedFile.exists());
	assertEquals(10, expectedFile.length());
	assertTrue(renamedFile.exists());
	assertEquals(0, renamedFile.length());

	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNameMask()).thenReturn("somedir/*.ext");
	final MoveFileCommand cmd = new MoveFileCommand(fnProvider, "otherdir/");
	cmd.upgrade(this.upgradeContext);
	assertFalse(expectedFile.exists());
	assertTrue(renamedFile.exists());
	assertEquals(10, renamedFile.length());
  }

  @Test(expected=UpgradeException.class)
  public void check_WildcardFileReplaceToFileInsteadofDirectory() throws IOException, UpgradeException
  {
	final File expectedFile = new File(this.pseudoRoot, "somedir/somefile.ext");
	final File renamedFile = new File(this.pseudoRoot, "otherdir/somefile.ext");
	installFileToRoot("somedir/somefile.ext");
	FileUtils.touch(renamedFile);
	assertTrue(expectedFile.exists());
	assertEquals(10, expectedFile.length());
	assertTrue(renamedFile.exists());
	assertEquals(0, renamedFile.length());

	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNameMask()).thenReturn("somedir/*.ext");
	final MoveFileCommand cmd = new MoveFileCommand(fnProvider, "otherdir/somefile.ext");
	cmd.upgrade(this.upgradeContext);
  }
  
  @Test
  public void check_MaskSpecifiedFileReplace() throws IOException, UpgradeException
  {
	installFileToRoot("input1_1.txt");
	installFileToRoot("input1_2.txt");
	installFileToRoot("input2_1.txt");
	final File expectedFile1 = new File(this.pseudoRoot, "input1_1.txt");
	final File expectedFile2 = new File(this.pseudoRoot, "input1_2.txt");
	final File expectedFile3 = new File(this.pseudoRoot, "input2_1.txt");
	final File movedFile1 = new File(this.pseudoRoot, "subdir/input1_1.txt");
	final File movedFile2 = new File(this.pseudoRoot, "subdir/input1_2.txt");
	final File movedFile3 = new File(this.pseudoRoot, "subdir/input2_1.txt");

	//TODO only works with existing directory
	//	FileUtils.forceMkdir(new File(this.pseudoRoot, "subdir"));

	assertTrue(expectedFile1.exists());
	assertTrue(expectedFile2.exists());
	assertTrue(expectedFile3.exists());

	assertFalse(movedFile1.exists());
	assertFalse(movedFile2.exists());
	assertFalse(movedFile3.exists());

	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNameMask()).thenReturn("input*.txt");
	final MoveFileCommand cmd = new MoveFileCommand(fnProvider, "subdir/");
	cmd.upgrade(this.upgradeContext);

	assertFalse(expectedFile1.exists());
	assertFalse(expectedFile2.exists());
	assertFalse(expectedFile3.exists());

	assertTrue(movedFile1.exists());
	assertTrue(movedFile2.exists());
	assertTrue(movedFile3.exists());
  }
  
  @Test
  public void check_regexSpecifiedFileReplace() throws IOException, UpgradeException
  {
	installFileToRoot("input1_1.txt");
	installFileToRoot("input1_2.txt");
	installFileToRoot("input2_1.txt");
	final File expectedFile1 = new File(this.pseudoRoot, "input1_1.txt");
	final File expectedFile2 = new File(this.pseudoRoot, "input1_2.txt");
	final File expectedFile3 = new File(this.pseudoRoot, "input2_1.txt");
	final File movedFile1 = new File(this.pseudoRoot, "subdir/input1_1_3.txt");
	final File movedFile2 = new File(this.pseudoRoot, "subdir/input1_2_3.txt");
	final File movedFile3 = new File(this.pseudoRoot, "subdir/input2_1_3.txt");

	//TODO only works with existing directory
	//	FileUtils.forceMkdir(new File(this.pseudoRoot, "subdir"));

	assertTrue(expectedFile1.exists());
	assertTrue(expectedFile2.exists());
	assertTrue(expectedFile3.exists());

	assertFalse(movedFile1.exists());
	assertFalse(movedFile2.exists());
	assertFalse(movedFile3.exists());

	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when(fnProvider.getFileNamePattern()).thenReturn("([^_]*)_1\\.txt");
	final MoveFileCommand cmd = new MoveFileCommand(fnProvider, "subdir/$1_1_3\\.txt");
	cmd.upgrade(this.upgradeContext);

	assertFalse(expectedFile1.exists());
	assertTrue(expectedFile2.exists());
	assertFalse(expectedFile3.exists());

	assertTrue(movedFile1.exists());
	assertFalse(movedFile2.exists());
	assertTrue(movedFile3.exists());
  }

}
