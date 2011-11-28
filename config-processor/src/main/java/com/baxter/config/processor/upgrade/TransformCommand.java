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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.baxter.config.om.ConfigID;
import com.baxter.config.processor.ProcessorContext;
import com.baxter.config.processor.desc.Descriptor;
import com.baxter.config.processor.desc.FilenameProvider;
import com.baxter.config.processor.impl.AbstractXSLTProcessor;

/**
 * @author xpdev
 * @since ${developmentVersion}
 */
public class TransformCommand extends AbstractFileCommand implements UpgradeCommand
{

  private static final DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();

  private final String stylesheet;

  protected TransformCommand(final FilenameProvider filenameProvider, final String stylesheet)
  {
	super(filenameProvider);
	this.stylesheet = stylesheet;
  }

  @Override
  public void upgrade(final UpgradeContext context) throws UpgradeException
  {

	final UpgradeXSLTProcessor processor = new UpgradeXSLTProcessor(context.getDescriptor());
	
	//FIXME create output stream!!
//	final OutputStream outputStream
	final ProcessorContext processorContext = new ProcessorContext()
	{
	  
	  @Override
	  public void setContentType(String contentType, String encoding)
	  {
	  }
	  
	  @Override
	  public OutputStream getOutputStream() throws IOException
	  {
		// TODO Auto-generated method stub
		return null;
	  }
	  
	  @Override
	  public ConfigID getConfigID()
	  {
		// TODO Auto-generated method stub
		return null;
	  }
	};
	processor.process( processorContext );

	try
	{
	  final URL stylesheetUrl = new URL(context.getStylesheetBase(), this.stylesheet);
	  final Source xslSource = new StreamSource(stylesheetUrl.openStream(), stylesheetUrl.toString());
	  final TransformerFactory transformerFactory = null;
	  final Templates xslTemplates = transformerFactory.newTemplates(xslSource);

	  final Source xmlSource = createXmlSource(context);
	  final StringWriter xsltWriter = new StringWriter();
	  final Result xsltResult = new StreamResult(xsltWriter);
	  final Transformer transformer = xslTemplates.newTransformer();
	  try
	  {
		transformer.transform(xmlSource, xsltResult);
		xsltWriter.flush();
		logger.debug("Upgrade XSLT output: {}", xsltWriter);
	  }
	  catch (final TransformerException e)
	  {
		throw new UpgradeException(e);
	  }
	}
	catch (final IOException e)
	{
	  throw new UpgradeException(e);
	}
  }

  private Source createXmlSource(final UpgradeContext context) throws UpgradeException
  {
	try
	{
	  final DocumentBuilder docBuilder = DBF.newDocumentBuilder();
	  final Document doc = docBuilder.newDocument();
	  final Element sourcesElement = doc.createElement("sources");
	  // iterate source files and add subelements
	  // Processor's repository root
	  final File repoDir = context.getProcessorRepositoryRoot();
	  final URL baseURL;
	  try
	  {
		baseURL = repoDir.toURI().toURL();
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

	  doc.appendChild(sourcesElement);
	  return new DOMSource(doc);
	}
	catch (final ParserConfigurationException e)
	{
	  throw new UpgradeException(e);
	}
	catch (final IOException e)
	{
	  throw new UpgradeException(e);
	}
  }

  private class UpgradeXSLTProcessor extends AbstractXSLTProcessor
  {

	protected UpgradeXSLTProcessor(final Descriptor descriptor)
	{
	  super(descriptor);
	}

  }

}
