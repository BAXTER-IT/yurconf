/*
 * Yurconf Object Model
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.om;

import java.util.Arrays;

/**
 * Version descriptor.
 *
 * @author ykryshchuk
 * @since 1.5
 */
public final class Version implements Comparable<Version>
{
  private final int[] parts;

  private Version(final int... parts)
  {
	this.parts = parts;
  }

  /**
   * Returns the Version object parsed from the input line.
   *
   * @param line
   *          the line with version where version parts are separated by dot
   * @return the version object (null for null line)
   * @throws IllegalArgumentException
   *           if the line does not represent a version string
   */
  public static Version valueOf(final String line)
  {
	if (line == null)
	{
	  return null;
	}
	else
	{
	  final String[] splitedVersion = line.split("[.]", -1);
	  final int[] parts = new int[splitedVersion.length];
	  if (splitedVersion.length == 0)
	  {
		throw new IllegalArgumentException(line + " is not a valid version format!");
	  }

	  try
	  {
		for (int i = 0; i < splitedVersion.length; i++)
		{
		  parts[i] = Integer.valueOf(splitedVersion[i]);
		}
	  }
	  catch (final NumberFormatException e)
	  {
		throw new IllegalArgumentException(line + " is not a valid version format!", e);
	  }

	  return new Version(parts);
	}
  }

  @Override
  public int compareTo(final Version other)
  {
	final int commonParts = Math.min(this.parts.length, other.parts.length);
	for (int i = 0; i < commonParts; i++)
	{
	  final int diff = this.parts[i] - other.parts[i];
	  if (diff != 0)
	  {
		return diff;
	  }
	}
	return this.parts.length - other.parts.length;
  }

  @Override
  public int hashCode()
  {
	final int prime = 31;
	int result = 1;
	result = prime * result + Arrays.hashCode(parts);
	return result;
  }

  @Override
  public boolean equals(final Object obj)
  {
	if (this == obj)
	{
	  return true;
	}
	if (obj == null)
	{
	  return false;
	}
	if (getClass() != obj.getClass())
	{
	  return false;
	}
	final Version other = (Version) obj;
	if (!Arrays.equals(parts, other.parts))
	{
	  return false;
	}
	return true;
  }

  @Override
  public String toString()
  {
	final StringBuilder sb = new StringBuilder();
	for (int i = 0; i < parts.length; i++)
	{
	  sb.append(parts[i]);

	  if (i < parts.length - 1)
	  {
		sb.append(".");
	  }
	}

	return sb.toString();
  }

  int[] getParts()
  {
	return this.parts;
  }

}
