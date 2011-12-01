/**
 * 
 */
package com.baxter.config.processor.upgrade;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.apache.commons.io.output.WriterOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.baxter.config.om.ConfigID;
import com.baxter.config.processor.ProcessorContext;
import com.baxter.config.processor.ProcessorException;
import com.baxter.config.processor.desc.Descriptor;
import com.baxter.config.processor.desc.FilenameProvider;
import com.baxter.config.processor.impl.XSLTProcessor;

/**
 * @author xpdev
 * @since ${developmentVersion}
 */
public class TransformCommand extends AbstractFileCommand implements UpgradeCommand
{
  private final String stylesheet;

  protected TransformCommand(final FilenameProvider filenameProvider, final String stylesheet)
  {
	super(filenameProvider);
	this.stylesheet = stylesheet;
  }

  @Override
  public void upgrade(final UpgradeContext context) throws UpgradeException
  {
	final UpgradeXSLTProcessor processor = new UpgradeXSLTProcessor(context.getDescriptor(), context);
	final StringWriter xsltWriter = new StringWriter();
	final OutputStream xsltStream = new WriterOutputStream(xsltWriter);
	final ProcessorContext processorContext = new ProcessorContext()
	{

	  @Override
	  public void setContentType(String contentType, String encoding)
	  {
	  }

	  @Override
	  public OutputStream getOutputStream() throws IOException
	  {
		return xsltStream;
	  }

	  @Override
	  public ConfigID getConfigID()
	  {
		// no config request - no config id
		return null;
	  }
	};

	try
	{
	  processor.process(processorContext);
	  xsltWriter.flush();
	  logger.debug("Upgrade XSLT output: {}", xsltWriter);
	}
	catch (final ProcessorException e)
	{
	  if (UpgradeException.class.isInstance(e))
	  {
		throw UpgradeException.class.cast(e);
	  }
	  else
	  {
		throw new UpgradeException(e);
	  }
	}
  }

  private class UpgradeXSLTProcessor extends XSLTProcessor
  {

	private final UpgradeContext upgradeContext;

	protected UpgradeXSLTProcessor(final Descriptor descriptor, final UpgradeContext upgradeContext)
	{
	  super(descriptor, upgradeContext.getProcessorFactory());
	  this.upgradeContext = upgradeContext;
	  setStylesheet(TransformCommand.this.stylesheet);

	}

	@Override
	protected Source getXmlSource(ProcessorContext context) throws ParserConfigurationException, ProcessorException
	{
	  final Source originalSource = super.getXmlSource(context);
	  if (DOMSource.class.isInstance(originalSource))
	  {
		final DOMSource domSource = DOMSource.class.cast(originalSource);
		final Document doc = Document.class.cast(domSource.getNode());
		final Element rootElement = doc.getDocumentElement();
		try
		{
		  final Element sourcesElement = doc.createElement("sources");
		  // iterate source files and add subelements
		  // Processor's repository root
		  final File repoDir = this.upgradeContext.getProcessorFactory().getRepository()
			  .getProductDirectory(this.upgradeContext.getDescriptor().getProductId());
		  final URL baseURL;
		  try
		  {
			baseURL = repoDir.toURI().toURL();
			sourcesElement.setAttribute("repo", baseURL.toString());
		  }
		  catch (final MalformedURLException e)
		  {
			throw new UpgradeException(e);
		  }
		  final List<String> filenames = listFilenames(baseURL);
		  for (String filename : filenames)
		  {
			final Element sourceElement = doc.createElement("source");
			final File sourceFile = new File(repoDir, filename);
			final URL sourceURL = sourceFile.toURI().toURL();
			sourceElement.setTextContent(sourceURL.toString());
			sourcesElement.appendChild(sourceElement);
		  }

		  rootElement.appendChild(sourcesElement);
		  return domSource;
		}
		catch (final IOException e)
		{
		  throw new UpgradeException(e);
		}
	  }
	  else
	  {
		logger.warn("The original source is not a DOM - {}", originalSource);
		return originalSource;
	  }
	}

  }

}
