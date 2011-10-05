/**
 * 
 */
package com.baxter.config.bean;

/**
 * @author ykryshchuk
 *
 */
public class NewChannelBean
{

  private String type = "T";
  private String name = "[new-name]";
  private String alias = "[new-alias]";
  private int jmsIndex = 1;
  public String getType()
  {
    return type;
  }
  public void setType(String type)
  {
    this.type = type;
  }
  public String getName()
  {
    return name;
  }
  public void setName(String name)
  {
    this.name = name;
  }
  public String getAlias()
  {
    return alias;
  }
  public void setAlias(String alias)
  {
    this.alias = alias;
  }
  public int getJmsIndex()
  {
    return jmsIndex;
  }
  public void setJmsIndex(int jmsIndex)
  {
    this.jmsIndex = jmsIndex;
  }
  
}
