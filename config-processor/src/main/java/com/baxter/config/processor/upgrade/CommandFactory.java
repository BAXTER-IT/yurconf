/**
 * 
 */
package com.baxter.config.processor.upgrade;

import com.baxter.config.processor.desc.AbstractUpgradeFile;
import com.baxter.config.processor.desc.UpgradeAddFile;
import com.baxter.config.processor.desc.UpgradeMoveFile;
import com.baxter.config.processor.desc.UpgradeRemoveFile;
import com.baxter.config.processor.desc.UpgradeTransform;

/**
 * @author xpdev
 * @since ${developmentVersion}
 */
public class CommandFactory
{

  private static final CommandFactory INSTANCE = new CommandFactory();

  private CommandFactory()
  {

  }

  public static final CommandFactory getInstance()
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
