/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.processor.desc;

/**
 * @author xpdev
 * @since 1.5
 */
public interface FilenameProvider
{

  String getFileNameMask();

  String getFileNamePattern();
}
