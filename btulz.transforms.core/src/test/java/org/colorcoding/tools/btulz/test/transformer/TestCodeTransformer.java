package org.colorcoding.tools.btulz.test.transformer;

import java.io.File;
import java.util.UUID;

import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.test.Environment;
import org.colorcoding.tools.btulz.transformer.CodeTransformer;

import junit.framework.TestCase;

/**
 * 代码转换器测试
 *
 * 覆盖： - CodeTransformer属性设置与参数添加 - CodeTransformer.transform()代码生成（需要外部依赖环境）
 */
public class TestCodeTransformer extends TestCase {

	/** CodeTransformer属性设置与参数添加 */
	public void testCodeTransformer_Properties() {
		CodeTransformer transformer = new CodeTransformer();
		transformer.setTemplateFolder("/templates/ibas_classic");
		transformer.setOutputFolder("/output");
		transformer.setGroupId("org.colorcoding");
		transformer.setArtifactId("ibas");
		transformer.setProjectVersion("0.0.1");
		transformer.setProjectUrl("http://colorcoding.org");
		transformer.addParameters(new Parameter("Company", "CC"));
		transformer.addParameters(new Parameter("ibasVersion", "0.1.2"));

		assertEquals("/templates/ibas_classic", transformer.getTemplateFolder());
		assertEquals("/output", transformer.getOutputFolder());
		assertEquals("org.colorcoding", transformer.getGroupId());
		assertEquals("ibas", transformer.getArtifactId());
		assertNotNull(transformer.getParameters());
	}

	/** Eclipse代码模板转换（需要外部环境） */
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
		codeTransformer.addDomains(Environment.getCodeFolder()
				+ "/ibas.initialfantasy/ibas.initialfantasy/src/main/resources/datastructures".replace("/",
						File.separator));
		codeTransformer.transform();
	}

}
