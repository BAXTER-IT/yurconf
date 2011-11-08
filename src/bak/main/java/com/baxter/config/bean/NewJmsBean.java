/**
 * 
 */
package com.baxter.config.bean;

/**
 * @author ykryshchuk
 * 
 */
public class NewJmsBean
{
  private String host = "[new-hostname]";
  private int port = -1;
  private String router = "[new-router]";
  private String username = "[new-user]";
  private String password = "";

  private Messages messages;

  public Messages getMessages()
  {
	return messages;
  }

  public void setMessages(Messages messages)
  {
	this.messages = messages;
  }

  public String getHost()
  {
	return host;
  }

  public void setHost(String host)
  {
	this.host = host;
  }

  public String getPort()
  {
	return String.valueOf(port);
  }

  public void setPort(String port)
  {
	try
	{
	  this.port = Integer.parseInt(port);
	}
	catch (final Exception e)
	{
	  if (messages != null)
	  {
		messages.add(e);
	  }
	  if (RuntimeException.class.isInstance(e))
	  {
		throw RuntimeException.class.cast(e);
	  }
	  else
	  {
		throw new RuntimeException(e);
	  }
	}
  }

  public int getPortNumber()
  {
	return this.port;
  }

  public String getRouter()
  {
	return router;
  }

  public void setRouter(String router)
  {
	this.router = router;
  }

  public String getUsername()
  {
	return username;
  }

  public void setUsername(String username)
  {
	this.username = username;
  }

  public String getPassword()
  {
	return password;
  }

  public void setPassword(String password)
  {
	this.password = password;
  }
}
