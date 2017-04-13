package org.colorcoding.tools.btulz.test.transformers;

import java.util.UUID;

import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.test.Environment;
import org.colorcoding.tools.btulz.transformers.CodeTransformer;

import junit.framework.TestCase;

public class testCodeTransformer extends TestCase {

	public void testEclipseCode() throws Exception {
		CodeTransformer codeTransformer = new CodeTransformer();
		codeTransformer.setTemplateFolder("eclipse/ibas_classic");
		codeTransformer.setOutputFolder(Environment.getOutputFolder());
		codeTransformer.setGroupId("org.colorcoding");
		codeTransformer.setArtifactId("ibas");
		codeTransformer.setProjectId(UUID.randomUUID().toString());
		codeTransformer.setProjectVersion("0.0.1");
		codeTransformer.setProjectUrl("http://colorcoding.org");
		codeTransformer.addParameters(new Parameter("Company", "CC"));
		codeTransformer.addParameters(new Parameter("ibasVersion", "0.1.2"));
		codeTransformer.addParameters(new Parameter("ProjectId", UUID.randomUUID().toString()));
		// codeTransformer.addDomains(Environment.getWorkingFolder() +
		// File.separator + Environment.getXmlModelsFileOld());

		codeTransformer.addDomains("E:\\MyWorks\\ColorCoding\\DS_TrainingTesting.xml");
		// codeTransformer.addDomains(
		// "E:\\MyWorks\\ColorCoding\\ibas.initialfantasy\\ibas.initialfantasy\\src\\main\\resources\\datastructures");
		// codeTransformer.addDomains(Environment.getWorkingFolder()
		// +
		// "/out/TrainingTesting/ibas.trainingtesting/src/main/resources/datastructures".replace("/",
		// File.separator));
		codeTransformer.transform();
	}

	public void testSampleCode() throws Exception {
		CodeTransformer codeTransformer = new CodeTransformer();
		codeTransformer.setTemplateFolder("E:\\MyWorks\\ColorCoding\\btulz4ibcp\\code\\eclipse\\ibcp_160911");
		codeTransformer.setOutputFolder(Environment.getOutputFolder());
		codeTransformer.setGroupId("org.colorcoding");
		codeTransformer.setArtifactId("ibas");
		codeTransformer.setProjectId(UUID.randomUUID().toString());
		codeTransformer.setProjectVersion("0.0.1");
		codeTransformer.setProjectUrl("http://colorcoding.org");
		codeTransformer.addParameters(new Parameter("ibasVersion", "0.1.1"));
		codeTransformer.addDomains(Environment.getDomainFolder());
		// codeTransformer.addDomain((new testModels()).createDomain());

		codeTransformer.transform();
	}
}
