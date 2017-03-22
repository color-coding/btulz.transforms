package org.colorcoding.tools.btulz.bobas.test.transformers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.ibas.bobas.core.RepositoryException;
import org.colorcoding.tools.btulz.bobas.transformers.ClassLoader4bobas;
import org.colorcoding.tools.btulz.bobas.transformers.DataTransformer;
import org.colorcoding.tools.btulz.bobas.transformers.DataTransformer4Jar;
import org.colorcoding.tools.btulz.transformers.TransformException;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

public class testDataTransformer extends TestCase {

	public void testClassLoader() throws ClassNotFoundException, IOException {
		File folder = new File(MyConfiguration.getStartupFolder());
		folder = folder.getParentFile().getParentFile().getParentFile().getParentFile();
		File classFolder = new File(String.format("%1$s%2$s%3$s%2$s%3$s%2$starget%2$sclasses", folder.getPath(),
				File.separator, "ibas.initialfantasy"));
		File jarFile = new File(String.format("%1$s%2$s%3$s%2$srelease%2$sbobas.businessobjectscommon-0.1.2.jar",
				folder.getPath(), File.separator, "ibas-framework"));
		ClassLoader parentLoader = Thread.currentThread().getContextClassLoader();
		ClassLoader4bobas loader = new ClassLoader4bobas(
				new URL[] { classFolder.toURI().toURL(), jarFile.toURI().toURL() }, parentLoader);
		loader.init();
		for (Entry<String, URL> item : loader.getClassesMap().entrySet()) {
			System.out.println(item);
			loader.loadClass(item.getKey());
		}
		loader.close();
	}

	public void testTransformer() throws ClassNotFoundException, TransformException, RepositoryException, IOException,
			SAXException, ParserConfigurationException, JAXBException {
		File folder = new File(MyConfiguration.getStartupFolder());
		folder = folder.getParentFile().getParentFile().getParentFile().getParentFile();
		String ifFolder = folder.getPath() + File.separator + "ibas.initialfantasy";
		String ibas = String.format("%1$s%2$s%3$s%2$srelease%2$s", folder.getPath(), File.separator, "ibas-framework");
		String config = String.format("%s%2$sibas.initialfantasy%2$sapp.xml", ifFolder, File.separator);
		String data = String.format("%s%2$srelease%2$sibas.initialfantasy-0.0.1.jar", ifFolder, File.separator);
		String classes = String.format("%s%2$srelease%2$sibas.initialfantasy-0.0.1.jar", ifFolder, File.separator);
		DataTransformer transformer = new DataTransformer();
		transformer.setConfigFile(config);
		transformer.setDataFile(data);
		transformer.addLibrary(new File(classes).toURI().toURL());
		for (File item : new File(ibas).listFiles()) {
			if (item.getName().endsWith(".jar")) {
				transformer.addLibrary(item.toURI().toURL());
			}
		}
		transformer.transform();

		data = String.format("%s%2$sibas.initialfantasy%2$starget%2$sclasses%2$sinitialization", ifFolder,
				File.separator);
		classes = String.format("%s%2$sibas.initialfantasy%2$starget%2$sclasses", ifFolder, File.separator);
		transformer = new DataTransformer4Jar();
		transformer.setConfigFile(config);
		transformer.setDataFile(data);
		transformer.addLibrary(new File(classes).toURI().toURL());
		for (File item : new File(ibas).listFiles()) {
			if (item.getName().endsWith(".jar")) {
				transformer.addLibrary(item.toURI().toURL());
			}
		}
		transformer.transform();
	}
}
