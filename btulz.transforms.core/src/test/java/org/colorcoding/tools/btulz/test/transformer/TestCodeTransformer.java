package org.colorcoding.tools.btulz.test.transformer;

import java.io.File;
import java.util.UUID;

import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.test.Environment;
import org.colorcoding.tools.btulz.transformer.CodeTransformer;
import org.colorcoding.tools.btulz.util.NamingRules;

import junit.framework.TestCase;

public class TestCodeTransformer extends TestCase {

	public void testNaming() {

		System.out.println(NamingRules.RULES_NAME_CAMEL_CASE_LOWER + ":");
		System.out.println(NamingRules.format(NamingRules.RULES_NAME_CAMEL_CASE_LOWER, "BOCode"));
		System.out.println(NamingRules.format(NamingRules.RULES_NAME_CAMEL_CASE_LOWER, "BOE"));
		System.out.println(NamingRules.format(NamingRules.RULES_NAME_CAMEL_CASE_LOWER, "ItemCode"));
		System.out.println(NamingRules.format(NamingRules.RULES_NAME_CAMEL_CASE_LOWER, "itemName"));
		System.out.println(NamingRules.format(NamingRules.RULES_NAME_CAMEL_CASE_LOWER, "boCode"));
		System.out.println(NamingRules.format(NamingRules.RULES_NAME_CAMEL_CASE_LOWER, "boe"));

		System.out.println(NamingRules.RULES_NAME_CAMEL_CASE_UPPER + ":");
		System.out.println(NamingRules.format(NamingRules.RULES_NAME_CAMEL_CASE_UPPER, "BOCode"));
		System.out.println(NamingRules.format(NamingRules.RULES_NAME_CAMEL_CASE_UPPER, "BOE"));
		System.out.println(NamingRules.format(NamingRules.RULES_NAME_CAMEL_CASE_UPPER, "ItemCode"));
		System.out.println(NamingRules.format(NamingRules.RULES_NAME_CAMEL_CASE_UPPER, "itemName"));
		System.out.println(NamingRules.format(NamingRules.RULES_NAME_CAMEL_CASE_UPPER, "boCode"));
		System.out.println(NamingRules.format(NamingRules.RULES_NAME_CAMEL_CASE_UPPER, "boe"));
	}

	public void testEclipseCode() throws Exception {
		CodeTransformer codeTransformer = new CodeTransformer();
		codeTransformer.setTemplateFolder(Environment.getCodeFolder()
				+ "/btulz.transforms/btulz.transforms.core/src/main/resources/code/ibas_classic".replace("/",
						File.separator));
		codeTransformer.setOutputFolder(Environment.getOutputFolder());
		codeTransformer.setGroupId("org.colorcoding");
		codeTransformer.setArtifactId("ibas");
		codeTransformer.setProjectVersion("0.0.1");
		codeTransformer.setProjectUrl("http://colorcoding.org");
		codeTransformer.addParameters(new Parameter("Company", "CC"));
		codeTransformer.addParameters(new Parameter("ibasVersion", "0.1.2"));
		codeTransformer.addParameters(new Parameter("ibasIfVersion", "0.1.0"));
		codeTransformer.addParameters(new Parameter("ProjectId", UUID.randomUUID().toString()));
		// codeTransformer.addDomains(Environment.getWorkingFolder() + File.separator +
		// Environment.getXmlModelsFileOld());
		// codeTransformer.addDomains(Environment.getCodeFolder() + String.format(
		// "/ibas-typescript/test/apps/trainingtesting/resources/ds_trainingtesting.xml",
		// File.separator));
		codeTransformer.addDomains(Environment.getCodeFolder()
				+ "/ibas.initialfantasy/ibas.initialfantasy/src/main/resources/datastructures".replace("/",
						File.separator));
		codeTransformer.transform();
	}

	public void testSampleCode() throws Exception {
		CodeTransformer codeTransformer = new CodeTransformer();
		codeTransformer.setTemplateFolder("E:\\MyWorks\\ColorCoding\\btulz4ibcp\\code\\eclipse\\ibcp_160911");
		codeTransformer.setOutputFolder(Environment.getOutputFolder());
		codeTransformer.setGroupId("org.colorcoding");
		codeTransformer.setArtifactId("ibas");
		codeTransformer.setProjectVersion("0.0.1");
		codeTransformer.setProjectUrl("http://colorcoding.org");
		codeTransformer.addParameters(new Parameter("ibasVersion", "0.1.1"));
		codeTransformer.addParameters(new Parameter("ProjectId", UUID.randomUUID().toString()));
		codeTransformer.addDomains(Environment.getDomainFolder());
		// codeTransformer.addDomain((new testModels()).createDomain());

		codeTransformer.transform();
	}
}
