package org.colorcoding.tools.btulz.test.transformers;

import java.io.File;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.colorcoding.tools.btulz.models.Domain;
import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.test.Environment;
import org.colorcoding.tools.btulz.test.models.testModels;
import org.colorcoding.tools.btulz.transformers.MultiTransformException;
import org.colorcoding.tools.btulz.transformers.TransformException;
import org.colorcoding.tools.btulz.transformers.XmlTransformer;
import org.colorcoding.tools.btulz.transformers.XmlTransformerDom4j;

import junit.framework.TestCase;

public class testXmlTransformer extends TestCase {

	public void testSaveReadXml()
			throws ClassNotFoundException, TransformException, MultiTransformException, JAXBException {
		IDomain domain = (new testModels()).createDomain();
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(domain);
		xmlTransformer.setInterruptOnError(true);
		xmlTransformer.save();
		xmlTransformer.setKeepResults(false);
		xmlTransformer.load(Environment.getXmlModelsFileOld(), false);
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
		domain = (new testModels()).createDomain();
		xmlTransformer = new XmlTransformerDom4j();
		// xmlTransformer.setInterruptOnError(true);
		xmlTransformer.load(Environment.getXmlModelsFileOld(), false);
		xmlTransformer.setInterruptOnError(true);
		xmlTransformer.save(Environment.getWorkingFolder() + File.separator + "dom4j");
	}

	public void testOldXml() throws JAXBException, TransformException, MultiTransformException {
		XmlTransformer xmlTransformer = new XmlTransformer();
		// xmlTransformer.load(Environment.getStartupFolder() + old_xml_path +
		// File.separator + "domain_models_old.xml",
		// false);

		xmlTransformer.load(Environment.getXmlModelsFileOld(), false);
		JAXBContext context = JAXBContext.newInstance(Domain.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

		System.out.println("序列化输出：");
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			StringWriter writer = new StringWriter();
			marshaller.marshal(domain, writer);
			String oldXML = writer.toString();
			System.out.println(oldXML);
		}

	}

}
