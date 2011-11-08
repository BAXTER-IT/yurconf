/**
 * 
 */
package com.baxter.config.om;

/**
 * Version descriptor.
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class Version implements Comparable<Version>
{
  private final int[] parts;
  
  private Version( int... parts ) {
	this.parts = parts;
  }
  
  /**
   * Returns the Version object parsed from the input line.
   * @param line the line with version where version parts are separated by dot
   * @return the version object
   * @throws IllegalArgumentException if the line does not represent a version string
   */
  public static Version valueOf( final String line ) {
	// TODO implement parsing
	return null;
  }

  @Override
  public int compareTo(final Version other)
  {
	final int commonParts = Math.min( this.parts.length, other.parts.length );
	for ( int i = 0; i<commonParts; i++ ) {
	  final int diff = this.parts[i] - other.parts[i];
	  if ( diff != 0 ) {
		return diff;
	  }
	}
	return this.parts.length - other.parts.length;
  }
  
  @Override
  public boolean equals(final Object obj)
  {
    // TODO Auto-generated method stub
    return super.equals(obj);
  }
  
  @Override
  public int hashCode()
  {
    // TODO Auto-generated method stub
    return super.hashCode();
  }
  
  @Override
  public String toString()
  {
    // TODO Auto-generated method stub
    return super.toString();
  }
  
  int[] getParts() {
	return this.parts;
  }
  
}
