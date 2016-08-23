package org.colorcoding.tools.btulz.transformers.test;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.models.Domain;
import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.transformers.MultiTransformException;
import org.colorcoding.tools.btulz.transformers.TransformException;
import org.colorcoding.tools.btulz.transformers.XmlTransformer;

import junit.framework.TestCase;

public class testXmlTransformer extends TestCase {

	public void testOldXml() throws JAXBException, TransformException, MultiTransformException {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getStartupFolder() + "domain_models_old.xml", false);

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
