/**
 * 
 */
package com.baxter.config.model;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author ykryshchuk
 * 
 */
public abstract class AbstractContainer
{

  @XmlElement(name = "pretag", namespace = Properties.NS)
  private List<Alias> aliases = new ArrayList<Alias>();

  @XmlElement(name = "entry", namespace = Properties.NS)
  private List<Entry> entries = new ArrayList<Entry>();

  @XmlElement(name = "group", namespace = Properties.NS)
  private List<Group> groups = new ArrayList<Group>();

  public Alias addAlias(final String key, final String value)
  {
	final Alias alias = new Alias(key, value);
	this.aliases.add(alias);
	return alias;
  }

  public Entry addEntryWithValue(final String key, final String value)
  {
	final Entry entry = Entry.forValue(key, value);
	this.entries.add(entry);
	return entry;
  }

  public Entry addEntryWithAlias(final String key, final String alias)
  {
	final Entry entry = Entry.forAlias(key, alias);
	this.entries.add(entry);
	return entry;
  }

  public Group getGroup(final String key) throws NoSuchElementException
  {
	for (Group group : this.groups)
	{
	  if (group.getKey().equals(key))
	  {
		return group;
	  }
	}
	throw new NoSuchElementException("no group for key " + key);
  }

  public Alias getAlias(final String key) throws NoSuchElementException
  {
	for (Alias alias : this.aliases)
	{
	  if (alias.getKey().equals(key))
	  {
		return alias;
	  }
	}
	throw new NoSuchElementException("no alias for key " + key);
  }

  public Entry getEntry(final String key) throws NoSuchElementException
  {
	for (Entry entry : this.entries)
	{
	  if (entry.getKey().equals(key))
	  {
		return entry;
	  }
	}
	throw new NoSuchElementException("no entry for key " + key);
  }
  
  public List<Group> getChannels() {
	final List<Group> channels = new ArrayList<Group>();
	for ( Group g : this.groups ) {
	  if ( g.getChannelType() != null ) {
		channels.add(g);
	  }
	}
	return channels;
  }

  protected <G extends Group> G addGroup(final G g)
  {
	this.groups.add(g);
	return g;
  }

  public Group addGroup(final String key)
  {
	final Group g = new Group(key);
	return addGroup(g);
  }

  public QueueGroup addQueue(final String key)
  {
	final QueueGroup g = new QueueGroup(key);
	return addGroup(g);
  }

  public TopicGroup addTopic(final String key)
  {
	final TopicGroup g = new TopicGroup(key);
	return addGroup(g);
  }

}
