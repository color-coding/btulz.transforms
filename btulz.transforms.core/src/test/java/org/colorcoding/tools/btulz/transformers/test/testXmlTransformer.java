package org.colorcoding.tools.btulz.transformers.test;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.transformers.XmlTransformer;

import junit.framework.TestCase;

public class testXmlTransformer extends TestCase {

	public void testOldXml() {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.input(Environment.getStartupFolder() + "domain_models_old.xml");

	}
}
