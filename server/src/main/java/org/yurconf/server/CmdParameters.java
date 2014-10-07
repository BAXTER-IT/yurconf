package org.yurconf.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class CmdParameters
{

  @Option(name = "-h", aliases = { "--host" }, metaVar = "HOST", required = false, usage = "hostUsage")
  private String host = "0.0.0.0";

  @Option(name = "-p", aliases = { "--port" }, metaVar = "PORT", required = false, usage = "portUsage")
  private int port = 4040;

  @Option(name = "-c", aliases = { "--context" }, metaVar = "CTX", required = false, usage = "contextUsage")
  private String context = "/";

  @Option(name = "-l", aliases = { "--location" }, metaVar = "LOCATION", required = false, usage = "locationUsage")
  private List<File> locations = new ArrayList<>();

  @Option(name = "-r", aliases = { "--repository" }, metaVar = "REPOSITORY", required = false, usage = "repositoryUsage")
  private File repository = new File(new File(System.getProperty("user.home")), ".yurconf/repository");

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

  public File getRepository()
  {
	return repository;
  }

  CmdLineParser getParser()
  {
	return parser;
  }

}
