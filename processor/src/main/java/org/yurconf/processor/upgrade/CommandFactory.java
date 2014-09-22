/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.processor.upgrade;

import org.yurconf.processor.desc.AbstractUpgradeFile;
import org.yurconf.processor.desc.UpgradeAddFile;
import org.yurconf.processor.desc.UpgradeMoveFile;
import org.yurconf.processor.desc.UpgradeRemoveFile;
import org.yurconf.processor.desc.UpgradeTransform;

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
