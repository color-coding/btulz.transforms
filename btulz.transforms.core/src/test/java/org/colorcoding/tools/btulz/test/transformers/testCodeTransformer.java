package org.colorcoding.tools.btulz.test.transformers;

import java.io.File;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.transformers.CodeTransformer;

import junit.framework.TestCase;

public class testCodeTransformer extends TestCase {

	private static String domain_file = testXmlTransformer.old_xml_path + File.separator + "domain_models_old.xml";

	public void testEclipseCode() throws Exception {
		CodeTransformer codeTransformer = new CodeTransformer();
		String tpltFolder = Environment.getResource("code/eclipse/ibas_classic").getPath();
		codeTransformer.setTemplateFolder("eclipse/ibas_classic");
		codeTransformer.setOutputFolder(System.getenv("TEMP"));
		codeTransformer.addDomains(Environment.getWorkingFolder() + domain_file);

		codeTransformer.transform();
	}
}
