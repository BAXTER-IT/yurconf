/**
 * 
 */
package com.baxter.config.processor.upgrade;

/**
 * @author xpdev
 * @since ${developmentVersion}
 */
public interface UpgradeCommand
{

  void upgrade(UpgradeContext context) throws UpgradeException;
  
}
