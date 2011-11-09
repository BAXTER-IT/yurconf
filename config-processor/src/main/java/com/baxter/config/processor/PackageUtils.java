package com.baxter.config.processor;

import java.util.jar.*;
import java.util.*;
import java.io.*;

public class PackageUtils
{
  public List<String> getClasseNamesInPackage(String jarName, String packageName) throws FileNotFoundException, IOException
  {
	List<String> items = new ArrayList<String>();

	packageName = packageName.replaceAll("\\.", "/");

	JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
	JarEntry jarEntry;

	while (true)
	{
	  jarEntry = jarFile.getNextJarEntry();
	  if (jarEntry == null)
	  {
		break;
	  }
	  if ((jarEntry.getName().startsWith(packageName)) && !jarEntry.isDirectory())
	  {
		items.add("/" + jarEntry.getName());
	  }
	}

	return items;
  }

  /**
   * @throws IOException 
   * @throws FileNotFoundException 
  *
  */
  public static void main(String[] args) throws FileNotFoundException, IOException
  {
	List<String> list = (new PackageUtils()).getClasseNamesInPackage("d:/Arpi/TestJar\\TestJar.jar", "META-INF.config.default");
	System.out.println(list);

	Scanner sc = new Scanner(PackageUtils.class.getClass()
	    .getResourceAsStream("/META-INF/config/default/something/something.txt"));
	while (sc.hasNextLine())
	{
	  System.out.println(sc.nextLine());
	}

  }
}