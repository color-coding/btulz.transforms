package org.colorcoding.tools.btulz.bobas.test.transformer;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.ibas.bobas.common.Criteria;
import org.colorcoding.ibas.bobas.common.ICriteria;
import org.colorcoding.ibas.bobas.organization.OrganizationManager;
import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.bobas.transformer.ClassLoader4Transformer;
import org.colorcoding.tools.btulz.bobas.transformer.DataTransformer;
import org.colorcoding.tools.btulz.bobas.transformer.DataTransformer4Jar;

import junit.framework.TestCase;

/**
 * 数据转换器测试（bobas模块）
 *
 * 覆盖：
 * - ClassLoader4Transformer：自定义类加载器，加载JAR中的类，父加载器接口引用子加载器的类
 * - DataTransformer4Jar：从JAR包加载数据结构并转换
 *
 * 注意：依赖ibas.initialfantasy项目和ibas-framework
 */
public class TestDataTransformer extends TestCase {

	/** 类加载器测试：父优先委派，框架类由父加载器加载，业务类由子加载器加载 */
	@SuppressWarnings("unused")
	public void testClassLoader()
			throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
		File folder = new File(MyConfiguration.getStartupFolder());
		folder = folder.getParentFile().getParentFile().getParentFile().getParentFile();
		String ifPath = folder.getPath() + File.separator + "ibas.initialfantasy";
		File classFolder = new File(ifPath + File.separator + "release" + File.separator + "ibas.initialfantasy-0.2.0.jar");
		String fwPath = folder.getPath() + File.separator + "ibas-framework";
		File jarFile = new File(
				fwPath + File.separator + "release" + File.separator + "bobas.businessobjectscommon-0.2.0.jar");
		ClassLoader parentLoader = this.getClass().getClassLoader();
		ClassLoader4Transformer loader = new ClassLoader4Transformer(
				new URL[] { classFolder.toURI().toURL(), jarFile.toURI().toURL() }, parentLoader);
		// 框架类：父加载器能找到，由父加载器加载
		Class<?> type = loader.loadClass(Criteria.class.getName());
		assertTrue("框架类应由父加载器加载", !type.getClassLoader().equals(loader));
		ICriteria criteria = (ICriteria) type.newInstance();
		// 业务类：父加载器找不到，由子加载器加载
		type = loader.loadClass("org.colorcoding.ibas.bobas.organization.initial.OrganizationManager");
		assertTrue("业务类应由子加载器加载", type.getClassLoader().equals(loader));
		OrganizationManager manager = (OrganizationManager) type.newInstance();
		loader.close();
	}

	/** DataTransformer4Jar从JAR包加载数据结构并转换 */
	public void testTransformer() throws Exception {
		Environment.getLogger().debug("begin test.");
		File folder = new File(MyConfiguration.getStartupFolder());
		folder = folder.getParentFile().getParentFile().getParentFile().getParentFile();
		String ifFolder = folder.getPath() + File.separator + "ibas.initialfantasy";
		String ibas = folder.getPath() + File.separator + "ibas-framework" + File.separator + "release" + File.separator;
		String config = ifFolder + File.separator + "ibas.initialfantasy" + File.separator + "app.xml";
		String data = ifFolder + File.separator + "release" + File.separator + "ibas.initialfantasy-0.2.0.jar";
		String classes = ifFolder + File.separator + "release" + File.separator + "ibas.initialfantasy-0.2.0.jar";
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
	}
}
