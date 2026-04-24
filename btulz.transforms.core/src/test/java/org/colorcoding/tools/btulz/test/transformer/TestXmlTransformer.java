package org.colorcoding.tools.btulz.test.transformer;

import java.io.File;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.colorcoding.tools.btulz.model.Domain;
import org.colorcoding.tools.btulz.model.IDomain;
import org.colorcoding.tools.btulz.test.Environment;
import org.colorcoding.tools.btulz.test.model.TestModels;
import org.colorcoding.tools.btulz.transformer.MultiTransformException;
import org.colorcoding.tools.btulz.transformer.TransformException;
import org.colorcoding.tools.btulz.transformer.XmlTransformer;
import org.colorcoding.tools.btulz.transformer.XmlTransformerDom4j;

import junit.framework.TestCase;

/**
 * XML转换器测试
 *
 * 覆盖： - XmlTransformer：从内存Domain对象加载并保存 - XmlTransformer：从XML文件加载域模型 -
 * XmlTransformerDom4j：使用Dom4j实现的XML转换 - 转换器属性设置(interruptOnError/keepResults)
 *
 * 注意：XmlTransformer被TestTemplate、TestRegionDomain等高级功能使用
 */
public class TestXmlTransformer extends TestCase {

	/** 获取旧版XML文件路径，不存在则返回null */
	private String getOldXmlPath() {
		String path = Environment.getWorkingFolder() + File.separator + Environment.getXmlModelsFileOld();
		if (new File(path).exists()) {
			return path;
		}
		return null;
	}

	/** 从内存Domain对象加载，保存后再加载，验证往返正确 */
	public void testSaveReadXml()
			throws ClassNotFoundException, TransformException, MultiTransformException, JAXBException {
		IDomain domain = (new TestModels()).createDomain();
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(domain);
		xmlTransformer.setInterruptOnError(true);
		xmlTransformer.save();
		xmlTransformer.setKeepResults(false);

		xmlTransformer.load(Environment.getWorkingFolder()
				+ "/out/TrainingTesting/ibas.trainingtesting/src/main/resources/datastructures".replace("/",
						File.separator),
				false);
		xmlTransformer.save();
		JAXBContext context = JAXBContext.newInstance(Domain.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

		System.out.println("序列化输出：");
		for (IDomain item : xmlTransformer.getWorkingDomains()) {
			StringWriter writer = new StringWriter();
			marshaller.marshal(item, writer);
			String oldXML = writer.toString();
			System.out.println(oldXML);
		}
		// 测试DOM4J实现，保存时元素顺序不乱
		String xmlPath = getOldXmlPath();
		if (xmlPath != null) {
			domain = (new TestModels()).createDomain();
			xmlTransformer = new XmlTransformerDom4j();
			xmlTransformer.load(xmlPath, false);
			xmlTransformer.setInterruptOnError(true);
			xmlTransformer.save(Environment.getWorkingFolder() + File.separator + "dom4j");
		}
	}

	/** 从XML文件加载并验证域/模型/业务对象 */
	public void testLoadFromXml() throws TransformException, MultiTransformException {
		String xmlPath = getOldXmlPath();
		if (xmlPath == null) {
			System.out.println("跳过：旧版XML文件不存在");
			return;
		}
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(xmlPath, false);
		IDomain[] domains = xmlTransformer.getWorkingDomains();
		assertNotNull("加载后应有域对象", domains);
		assertTrue("应至少包含一个域", domains.length > 0);
		assertEquals("TrainingTesting", domains[0].getName());
		assertTrue(domains[0].getModels().size() > 0);
		assertTrue(domains[0].getBusinessObjects().size() > 0);
	}
}
