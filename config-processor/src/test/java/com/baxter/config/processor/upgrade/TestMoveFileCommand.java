/**
 * 
 */
package com.baxter.config.processor.upgrade;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * @author xpdev
 * @since ${developmentVersion}
 */
public class TestMoveFileCommand
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
}
