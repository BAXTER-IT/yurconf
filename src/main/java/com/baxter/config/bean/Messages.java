/**
 * 
 */
package com.baxter.config.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ykryshchuk
 * 
 */
public class Messages
{

  private List<String> items = new ArrayList<String>();

  public List<String> getItems()
  {
	return this.items;
  }
  
  public boolean isEmpty() {
	return this.items.isEmpty();
  }

  public void add(final Throwable t)
  {
	this.items.add(String.valueOf(t));
  }
  
  public void add( final String text ) {
	this.items.add(text);
  }

}
