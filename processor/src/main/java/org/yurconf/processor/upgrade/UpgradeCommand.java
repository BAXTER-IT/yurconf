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

/**
 * @author xpdev
 * @since 1.5
 */
public interface UpgradeCommand
{

  void upgrade(UpgradeContext context) throws UpgradeException;

}
