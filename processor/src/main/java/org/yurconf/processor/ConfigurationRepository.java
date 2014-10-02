/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 *
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 *
 * Join our team to help make it better.
 */
package org.yurconf.processor;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.yurconf.processor.desc.Descriptor;
import org.yurconf.processor.desc.Upgrade;

/**
 * @author yura
 *
 */
public interface ConfigurationRepository
{

  /**
   * Returns repository root directory.
   * @return directory
   */
  File getRoot();

  /**
   * Returns the root directory for specified product.
   *
   * @param productId
   *          product identifier
   * @return directory
   */
  File getProductDirectory(final String productId);

  /**
   * Loads a product processors descriptor from this repository.
   *
   * @param productId
   *          product identifier
   * @return processors descriptor
   * @throws ProcessorException
   *           if failed to load the descriptor from file
   * @throws RepositoryException
   *           if the descriptor file has not been found in repository
   */
  Descriptor getDescriptor(final String productId) throws ProcessorException;

  Iterator<Descriptor> getDescriptors() throws ProcessorException;

  /**
   * Upgrades the package in repository.
   *
   * @param descriptor
   *          the package descriptor
   * @param upgrade
   *          upgrade to execute
   * @throws ProcessorException
   *           if failed to upgrade
   */
  void upgradePackage(final Descriptor descriptor, final Upgrade upgrade, final ProcessorFactory processorFactory)
	  throws ProcessorException;

  /**
   * Install a configuration processor package into repository. Copies all necessary resources from processor package to a local
   * repository.
   *
   * @param descriptor
   *          the processor descriptor
   * @throws IOException
   *           if failed to copy a resource
   */
  void installPackage(final Descriptor descriptor) throws IOException, ProcessorException;
}
