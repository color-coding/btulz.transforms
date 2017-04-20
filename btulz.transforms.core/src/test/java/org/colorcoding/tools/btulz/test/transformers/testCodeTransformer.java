package org.colorcoding.tools.btulz.test.transformers;

import java.io.File;
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
		codeTransformer.addDomains(Environment.getCodeFolder() + String.format(
				"%1$sibas-typescript%1$stest%1$sapps%1$strainingtesting%1$sresources%1$sDS_TrainingTesting.xml",
				File.separator));
		// codeTransformer.addDomains(Environment.getCodeFolder() +
		// String.format(
		// "%1$sibas.initialfantasy%1$ssrc%1$smain%1$sresources%1$sdatastructures",
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
