package org.yurconf.server;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class CmdParametersTest
{

  @Test
  public void verifyDefaults() throws Exception {
	final CmdParameters cmd = CmdParameters.parse(new String[0]);
	assertEquals("0.0.0.0",cmd.getHost());
	assertEquals(4040, cmd.getPort());
	assertEquals("/", cmd.getContext());
	assertEquals(Collections.emptyList(), cmd.getLocations());
  }

  @Test
  public void multipleLocations() throws Exception
  {
	final String[] args = { "-l", "/opt/dir1", "-l", "/opt/dir2/subdir" };
	final CmdParameters cmd = CmdParameters.parse(args);
	assertEquals(Arrays.asList(new File("/opt/dir1"), new File("/opt/dir2/subdir")), cmd.getLocations());
  }

}
