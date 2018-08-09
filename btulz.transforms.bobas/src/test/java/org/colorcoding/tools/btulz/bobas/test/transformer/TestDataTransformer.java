package org.colorcoding.tools.btulz.bobas.test.transformer;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.ibas.bobas.common.Criteria;
import org.colorcoding.ibas.bobas.common.ICriteria;
import org.colorcoding.ibas.bobas.organization.IOrganizationManager;
import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.bobas.transformer.ClassLoader4Transformer;
import org.colorcoding.tools.btulz.bobas.transformer.DataTransformer;
import org.colorcoding.tools.btulz.bobas.transformer.DataTransformer4Jar;

import junit.framework.TestCase;

public class TestDataTransformer extends TestCase {

	@SuppressWarnings("unused")
	public void testClassLoader()
			throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
		File folder = new File(MyConfiguration.getStartupFolder());
		folder = folder.getParentFile().getParentFile().getParentFile().getParentFile();
		File classFolder = new File(String.format("%1$s%2$s%3$s%2$s%3$s%2$starget%2$sclasses", folder.getPath(),
				File.separator, "ibas.initialfantasy"));
		classFolder = new File(String.format("%1$s%2$s%3$s%2$srelease%2$sibas.initialfantasy-0.1.0.jar",
				folder.getPath(), File.separator, "ibas.initialfantasy"));
		File jarFile = new File(String.format("%1$s%2$s%3$s%2$srelease%2$sbobas.businessobjectscommon-0.1.2.jar",
				folder.getPath(), File.separator, "ibas-framework"));
		ClassLoader parentLoader = this.getClass().getClassLoader();
		ClassLoader4Transformer loader = new ClassLoader4Transformer(
				new URL[] { classFolder.toURI().toURL(), jarFile.toURI().toURL() }, parentLoader);
		int count = 0;
		for (String className : loader.getClassNames()) {
			// System.out.println(className);
			Class<?> type = loader.findClass(className);
			if (!type.getClassLoader().equals(loader)) {
				// 仅其他加载器加载类型
				System.err.println(type.getName());
			}
			count++;
		}
		System.err.println("class count:" + count);
		Class<?> type = null;
		// 测试基本类型的，能否引用
		// type = loader.findClass(Object.class.getName());
		// Object object = (Object) type.newInstance();
		// 测试是否能用接口接受类型实例，结论不行
		type = loader.findClass(Criteria.class.getName());
		ICriteria criteria = (ICriteria) type.newInstance();
		// 父加载器接口引用子加载器的类
		type = loader.findClass(IOrganizationManager.class.getName());
		type = loader.findClass("org.colorcoding.ibas.bobas.organization.fantasy.OrganizationManager");
		IOrganizationManager manager = (IOrganizationManager) type.newInstance();
		type = loader.findClass("org.colorcoding.ibas.initialfantasy.bo.organizations.OrganizationalStructure");
		Object org = type.newInstance();
		loader.close();
	}

	public void testTransformer() throws Exception {
		Environment.getLogger().debug("begin test.");
		File folder = new File(MyConfiguration.getStartupFolder());
		folder = folder.getParentFile().getParentFile().getParentFile().getParentFile();
		String ifFolder = folder.getPath() + File.separator + "ibas.initialfantasy";
		String ibas = String.format("%1$s%2$s%3$s%2$srelease%2$s", folder.getPath(), File.separator, "ibas-framework");
		String config = String.format("%s%2$sibas.initialfantasy%2$sapp.xml", ifFolder, File.separator);
		String data = String.format("%s%2$srelease%2$sibas.initialfantasy-0.1.0.jar", ifFolder, File.separator);
		String classes = String.format("%s%2$srelease%2$sibas.initialfantasy-0.1.0.jar", ifFolder, File.separator);
		DataTransformer transformer = new DataTransformer4Jar();
		transformer.setConfigFile(config);
		transformer.setDataFile(data);
		transformer.addLibrary(new File(classes).toURI().toURL());
		File[] files = new File(ibas).listFiles();
		if (files != null) {
			for (File item : files) {
				if (item.getName().endsWith(".jar")) {
					transformer.addLibrary(item.toURI().toURL());
				}
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
		files = new File(ibas).listFiles();
		if (files != null) {
			for (File item : files) {
				if (item.getName().endsWith(".jar")) {
					transformer.addLibrary(item.toURI().toURL());
				}
			}
		}
		// transformer.transform();
	}
}
