/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 *
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 *
 * Join our team to help make it better.
 */
package org.yurconf.processor.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.junit.Test;

public class TestYurconfProtocol {

	@Test
	public void detectCommonProtocols() throws Exception {
		assertNull("Should not intercept http",
				YurconfProtocol.forUri(new URI("http://yurconf.org/")));
		assertNull("Should not intercept ftp",
				YurconfProtocol.forUri(new URI("ftp://user@yurconf.org/")));
	}

	@Test
	public void detectProcessorResource() throws Exception {
		assertEquals(YurconfProtocol.PROCESSOR_RESOURCE, YurconfProtocol.forUri(new URI(
				"yurconf://@org.yurconf.log/logging.xsl")));
		assertEquals(YurconfProtocol.PROCESSOR_RESOURCE, YurconfProtocol.forUri(new URI(
				"yurconf://@org.yurconf.base/incl/repo.xsl")));
	}

	@Test
	public void detectRepositorySource() throws Exception {
		assertEquals(YurconfProtocol.REPOSITORY_SOURCE, YurconfProtocol.forUri(new URI(
				"yurconf://org.yurconf.log/log.xml")));
		assertEquals(YurconfProtocol.REPOSITORY_SOURCE,
				YurconfProtocol.forUri(new URI("yurconf:/topology.xml")));
		assertEquals(YurconfProtocol.REPOSITORY_SOURCE,
			YurconfProtocol.forUri(new URI("yurconf:subdir/root-product-file.xml")));
		assertEquals(YurconfProtocol.REPOSITORY_SOURCE,
			YurconfProtocol.forUri(new URI("yurconf:///very-root.xml")));
	}

}
