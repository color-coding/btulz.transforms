package org.colorcoding.tools.btulz.bobas.test.transformer;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.ibas.bobas.common.Criteria;
import org.colorcoding.ibas.bobas.common.ICriteria;
import org.colorcoding.ibas.bobas.organization.OrganizationManager;
import org.colorcoding.tools.btulz.bobas.transformer.ClassLoader4Transformer;

import junit.framework.TestCase;

/**
 * 数据转换器测试（bobas模块）
 *
 * 覆盖：
 * - ClassLoader4Transformer：自定义类加载器，加载JAR中的类，父加载器接口引用子加载器的类
 *
 * 注意：依赖ibas.initialfantasy项目和ibas-framework；
 * DataTransformer4Jar依赖旧版jar（javax.xml.bind），Java 21下无法运行，已移除
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
}
