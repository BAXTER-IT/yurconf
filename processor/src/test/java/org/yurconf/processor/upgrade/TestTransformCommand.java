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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.yurconf.processor.desc.FilenameProvider;
import org.yurconf.processor.upgrade.TransformCommand;

public class TestTransformCommand extends TestAbstractFileCommand
{

  /**
   * The test corresponds to a descriptor upgrade configuration, like: <upgrades> ... <from version="x" toVersion="y"> ...
   * <transform stylesheet="xsl_path" /> ... </from> ... </upgrade>
   *
   * @throws Exception
   */
  @Test
  public void check_renameListToItems() throws Exception
  {
	installFileToRoot("structure1.xml");

	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	final File expectedNewFile = new File(this.pseudoRoot, "structure1.xml");
	assertTrue(expectedNewFile.isFile());

	final TransformCommand cmd = new TransformCommand(fnProvider, "yurconf://@test.product/rename-list-to-items-noinput.xsl");
	cmd.upgrade(this.upgradeContext);

	assertTrue(expectedNewFile.isFile());
	final Element rootElement = loadXml(expectedNewFile);
	assertEquals("items", rootElement.getNodeName());
	final NodeList children = rootElement.getChildNodes();
	for (int i = 0; i < children.getLength(); i++)
	{
	  final Node child = children.item(i);
	  if (child.getNodeType() == Node.ELEMENT_NODE)
	  {
		assertEquals("item", child.getNodeName());
	  }
	  else
	  {
		assertEquals(Node.TEXT_NODE, child.getNodeType());
	  }
	}
  }

  @Test
  public void check_moveTextToAttributeWithInput() throws Exception
  {
	installFileToRoot("catalogue/input.xml");
	installFileToRoot("catalogue/input(1).xml");

	final FilenameProvider fnProvider = mock(FilenameProvider.class);
	when( fnProvider.getFileNameMask()).thenReturn("catalogue/input*.xml");

	final TransformCommand cmd = new TransformCommand(fnProvider, "move-text-to-attribute-withinput.xsl");
	cmd.upgrade(this.upgradeContext);

	final File inputResultFile = new File( this.pseudoRoot, "catalogue/converted-input.xml");
	assertTrue(inputResultFile.isFile());
	final Element inputResult = loadXml(inputResultFile);
	assertEquals("first element", getFirstChildElement(inputResult).getAttribute("value"));
	assertEquals("1", getFirstChildElement(inputResult).getAttribute("id"));

	final File input1ResultFile = new File( this.pseudoRoot, "catalogue/converted-input(1).xml");
	assertTrue(input1ResultFile.isFile());
	final Element input1Result = loadXml(input1ResultFile);
	assertEquals("overriden first element", getFirstChildElement(input1Result).getAttribute("value"));
	assertEquals("1", getFirstChildElement(input1Result).getAttribute("id"));

  }


  private Element loadXml( final File file ) throws Exception {
	final DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	final Document doc = docBuilder.parse(file);
	return doc.getDocumentElement();
	}

  private Element getFirstChildElement( final Element element ) {
	final NodeList children = element.getChildNodes();
	for (int i = 0; i < children.getLength(); i++)
	{
	  final Node child = children.item(i);
	  if (child.getNodeType() == Node.ELEMENT_NODE) {
		return (Element) child;
	  }
	}
	return null;
  }

}
