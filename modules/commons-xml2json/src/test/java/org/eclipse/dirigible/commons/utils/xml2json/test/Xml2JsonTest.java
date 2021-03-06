package org.eclipse.dirigible.commons.utils.xml2json.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.dirigible.commons.utils.xml2json.Xml2Json;
import org.junit.Test;

public class Xml2JsonTest {

	private void toJson(String xmlFile, String jsonFile) {
		try {
			InputStream inXml = Xml2JsonTest.class.getResourceAsStream(xmlFile);
			String xml = IOUtils.toString(inXml);
			Xml2Json xml2json = new Xml2Json();
			String json = xml2json.toJson(xml);
			System.out.println(xml);
			System.out.println(json);
			InputStream inJson = Xml2JsonTest.class.getResourceAsStream(jsonFile);
			String jsonExpected = IOUtils.toString(inJson);
			normalizeLineEndings(jsonExpected);
			normalizeLineEndings(json);
			assertEquals(jsonExpected, json);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private void toXml(String jsonFile, String xmlFile) {
		try {
			InputStream inJson = Xml2JsonTest.class.getResourceAsStream(jsonFile);
			String json = IOUtils.toString(inJson);
			Xml2Json xml2json = new Xml2Json();
			String xml = xml2json.toXml(json);
			xml = xml2json.prettyPrintXml(xml);
			System.out.println(json);
			System.out.println(xml);
			InputStream inXml = Xml2JsonTest.class.getResourceAsStream(xmlFile);
			String xmlExpected = IOUtils.toString(inXml);
			xmlExpected = normalizeLineEndings(xmlExpected);
			xml = normalizeLineEndings(xml);
			assertEquals(xmlExpected, xml);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private String normalizeLineEndings(String content) {
		content = content.replace("\r\n", "\n");
		content = content.replace("\n\r", "\n");
		content = content.replace("\r", "\n");
		return content;
	}

	@Test
	public void testToJsonBasic() {
		toJson("xml2json/basic.xml", "xml2json/basic.json");
	}

	@Test
	public void testToJsonAttrs() {
		toJson("xml2json/attrs.xml", "xml2json/attrs.json");
	}

	@Test
	public void testToJsonElementWithAttrs() {
		toJson("xml2json/element_attrs.xml", "xml2json/element_attrs.json");
	}

	@Test
	public void testToJsonElementWithAttrsMultiple() {
		toJson("xml2json/element_attrs_multiple.xml", "xml2json/element_attrs_multiple.json");
	}

	@Test
	public void testToJsonCData() {
		toJson("xml2json/cdata.xml", "xml2json/cdata.json");
	}

	@Test
	public void testToJsonCDataAttrs() {
		toJson("xml2json/cdata_attrs.xml", "xml2json/cdata_attrs.json");
	}

	@Test
	public void testToJsonArray() {
		toJson("xml2json/array.xml", "xml2json/array.json");
	}

	@Test
	public void testToXmlBasic() {
		toXml("xml2json/basic.json", "xml2json/basic.xml");
	}

	@Test
	public void testToXmlAttrs() {
		toXml("xml2json/attrs.json", "xml2json/attrs.xml");
	}

	@Test
	public void testToXmlElementWithAttrs() {
		toXml("xml2json/element_attrs.json", "xml2json/element_attrs.xml");
	}

	@Test
	public void testToXmlElementWithAttrsMultiple() {
		toXml("xml2json/element_attrs_multiple.json", "xml2json/element_attrs_multiple.xml");
	}

	@Test
	public void testToXmlCData() {
		toXml("xml2json/cdata.json", "xml2json/cdata.xml");
	}

	@Test
	public void testToXmlCDataAttrs() {
		toXml("xml2json/cdata_attrs.json", "xml2json/cdata_attrs.xml");
	}

	@Test
	public void testToXmlArray() {
		toXml("xml2json/array.json", "xml2json/array.xml");
	}

}
