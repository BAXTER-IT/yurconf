/**
 * 
 */
package com.baxter.config.servlet;

import java.util.ArrayList;
import java.util.List;

import com.baxter.config.model.Properties;

/**
 * @author ykryshchuk
 *
 */
public class PropertiesPersister
{
  private Properties properties;
  
  private String tag;
  
  private String storedTag;

  public Properties getProperties()
  {
    return properties;
  }

  public void setProperties(Properties properties)
  {
    this.properties = properties;
  }

  public String getTag()
  {
    return tag;
  }

  public void setTag(String tag)
  {
    this.tag = tag;
  }
  
  public String getStoredTag()
  {
    return storedTag;
  }

  public void setStoredTag(String storedTag)
  {
    this.storedTag = storedTag;
  }

  public List<String> getStoredTags() {
	final List<String> tags = new ArrayList<String>();
	
	return tags;
  }
  
}
