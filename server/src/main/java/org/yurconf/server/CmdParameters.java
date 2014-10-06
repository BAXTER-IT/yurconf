package org.yurconf.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class CmdParameters
{

  @Option(name = "-h", aliases = { "--host" }, metaVar = "HOST", required = false, usage = "Host name to bind Yurconf Server, default is 0.0.0.0")
  private String host = "0.0.0.0";

  @Option(name = "-p", aliases = { "--port" }, metaVar = "PORT", required = false, usage = "Port to listen by Yurconf Server, default is 4040")
  private int port = 4040;

  @Option(name = "-c", aliases = { "--context" }, metaVar = "CTX", required = false, usage = "Yurconf Server context name, defauls to '/'")
  private String context = "/";

  @Option(name = "-l", aliases = { "--location" }, metaVar = "LOCATION", required = false, usage = "Location of Configuration Processor Archives")
  private List<File> locations = new ArrayList<>();

  private final CmdLineParser parser = new CmdLineParser(this);

  private CmdParameters(final String[] args) throws CmdLineException
  {
	parser.parseArgument(args);
  }

  static CmdParameters parse(final String[] args) throws CmdLineException
  {
	return new CmdParameters(args);
  }

  public String getHost()
  {
	return host;
  }

  public int getPort()
  {
	return port;
  }

  public String getContext()
  {
	return context;
  }

  public List<File> getLocations()
  {
	return locations;
  }

}
