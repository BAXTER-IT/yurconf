/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.processor.upgrade;

import com.baxter.config.processor.desc.AbstractUpgradeFile;
import com.baxter.config.processor.desc.UpgradeAddFile;
import com.baxter.config.processor.desc.UpgradeMoveFile;
import com.baxter.config.processor.desc.UpgradeRemoveFile;
import com.baxter.config.processor.desc.UpgradeTransform;

/**
 * @author xpdev
 * @since 1.5
 */
public final class CommandFactory
{

  private static final CommandFactory INSTANCE = new CommandFactory();

  private CommandFactory()
  {

  }

  public static CommandFactory getInstance()
  {
	return INSTANCE;
  }

  public UpgradeCommand getCommand(final AbstractUpgradeFile commandBean)
  {
	// TODO review
	if (UpgradeAddFile.class.isInstance(commandBean))
	{
	  return new AddFileCommand(UpgradeAddFile.class.cast(commandBean));
	}
	if (UpgradeRemoveFile.class.isInstance(commandBean))
	{
	  return new RemoveFileCommand(UpgradeRemoveFile.class.cast(commandBean));
	}
	if (UpgradeMoveFile.class.isInstance(commandBean))
	{
	  return new MoveFileCommand(UpgradeMoveFile.class.cast(commandBean), UpgradeMoveFile.class.cast(commandBean).getTo());
	}
	if (UpgradeTransform.class.isInstance(commandBean))
	{
	  return new TransformCommand(UpgradeTransform.class.cast(commandBean), UpgradeTransform.class.cast(commandBean).getStylesheet());
	}
	throw new IllegalArgumentException("Unsupported upgrade descriptor " + commandBean);
  }

}
