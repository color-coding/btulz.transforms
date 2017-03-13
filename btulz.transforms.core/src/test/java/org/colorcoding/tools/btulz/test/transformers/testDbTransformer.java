package org.colorcoding.tools.btulz.test.transformers;

import java.io.File;

import org.colorcoding.tools.btulz.test.Environment;
import org.colorcoding.tools.btulz.transformers.DsTransformer;
import org.colorcoding.tools.btulz.transformers.DsTransformer4Jar;
import org.colorcoding.tools.btulz.transformers.JarTransformer;
import org.colorcoding.tools.btulz.transformers.SqlTransformer;
import org.colorcoding.tools.btulz.transformers.SqlTransformer4Jar;

import junit.framework.TestCase;

public class testDbTransformer extends TestCase {

	/**
	 * 测试数据结构创建
	 * 
	 * @throws Exception
	 */
	public void testDS() throws Exception {
		System.err.println("运行请清理test-classes目录的历史文件。");
		DsTransformer dsTransformer = new DsTransformer();
		dsTransformer.setTemplateFile("ds_mssql_ibas_classic.xml");
		// dsTransformer.addDomains(Environment.getXmlModelsFileOld());
		dsTransformer.addDomains("E:\\MyWorks\\ColorCoding\\models");
		dsTransformer.setCompany("CC");
		dsTransformer.setDbServer("ibas-dev-mssql");
		dsTransformer.setDbPort("1433");
		dsTransformer.setDbSchema("dbo");
		dsTransformer.setDbName("ibas_demo" + "_" + dsTransformer.hashCode());
		dsTransformer.setDbUser("sa");
		dsTransformer.setDbPassword("1q2w3e");
		dsTransformer.transform();
	}

	public void testInitMSSQL() throws Exception {
		File file = new File(Environment.getSqlsFolder() + File.separator + "sql_mssql_ibas_initialization.xml");
		if (file.exists() && file.isFile()) {
			SqlTransformer sqlTransformer = new SqlTransformer();
			sqlTransformer.setSqlFile(file.getPath());
			sqlTransformer.setCompany("CC");
			sqlTransformer.setDbServer("ibas-dev-mssql");
			sqlTransformer.setDbPort("1433");
			sqlTransformer.setDbName("ibas_demo" + String.format("_%s", this.hashCode()));
			sqlTransformer.setDbSchema("dbo");
			sqlTransformer.setDbUser("sa");
			sqlTransformer.setDbPassword("1q2w3e");
			sqlTransformer.transform();
		}
	}

	public void testInitMYSQL() throws Exception {
		File file = new File(Environment.getSqlsFolder() + File.separator + "sql_mysql_ibas_initialization.xml");
		if (file.exists() && file.isFile()) {
			SqlTransformer sqlTransformer = new SqlTransformer();
			sqlTransformer.setSqlFile(file.getPath());
			sqlTransformer.setCompany("CC");
			sqlTransformer.setDbServer("ibas-dev-mysql");
			sqlTransformer.setDbPort("3306");
			sqlTransformer.setDbName("ibas_demo" + String.format("_%s", this.hashCode()));
			sqlTransformer.setDbUser("root");
			sqlTransformer.setDbPassword("1q2w3e");
			sqlTransformer.transform();
		}
	}

	public void testInitPGSQL() throws Exception {
		File file = new File(Environment.getSqlsFolder() + File.separator + "sql_pgsql_ibas_initialization.xml");
		if (file.exists() && file.isFile()) {
			SqlTransformer sqlTransformer = new SqlTransformer();
			sqlTransformer.setSqlFile(file.getPath());
			sqlTransformer.setCompany("CC");
			sqlTransformer.setDbServer("ibas-dev-pgsql");
			sqlTransformer.setDbPort("5432");
			sqlTransformer.setDbName("ibas_demo" + String.format("_%s", this.hashCode()));
			sqlTransformer.setDbUser("postgres");
			sqlTransformer.setDbPassword("1q2w3e");
			sqlTransformer.transform();
		}
	}

	public void testInitHANA() throws Exception {
		File file = new File(Environment.getSqlsFolder() + File.separator + "sql_hana_ibas_initialization.xml");
		if (file.exists() && file.isFile()) {
			SqlTransformer sqlTransformer = new SqlTransformer();
			sqlTransformer.setSqlFile(file.getPath());
			sqlTransformer.setCompany("CC");
			sqlTransformer.setDbServer("ibas-dev-hana");
			sqlTransformer.setDbPort("30015");
			sqlTransformer.setDbName("ibas_demo" + String.format("_%s", this.hashCode()));
			sqlTransformer.setDbUser("SYSTEM");
			sqlTransformer.setDbPassword("AVAtech2015!");
			sqlTransformer.transform();
		}
	}

	/**
	 * 测试jar包中数据结构创建
	 * 
	 * @throws Exception
	 */
	public void testDsJar() throws Exception {
		System.err.println("运行请清理test-classes目录的历史文件。");
		File folder = new File(Environment.getSqlsFolder());
		for (File file : folder.listFiles()) {
			DsTransformer4Jar dsTransformer = new DsTransformer4Jar();
			dsTransformer.setTemplateFile("ds_mssql_ibas_classic.xml");
			dsTransformer.addDomains(file);
			dsTransformer.setCompany("CC");
			dsTransformer.setDbServer("ibas-dev-mssql");
			dsTransformer.setDbPort("1433");
			dsTransformer.setDbSchema("dbo");
			dsTransformer.setDbName("ibas_demo" + "_" + this.hashCode());
			dsTransformer.setDbUser("sa");
			dsTransformer.setDbPassword("1q2w3e");
			dsTransformer.transform();
		}
	}

	/**
	 * 测试jar包中sql执行
	 * 
	 * @throws Exception
	 */
	public void testSqlJar() throws Exception {
		System.err.println("运行请清理test-classes目录的历史文件。");
		File folder = new File(Environment.getSqlsFolder());
		for (File file : folder.listFiles()) {
			SqlTransformer4Jar sqlTransformer = new SqlTransformer4Jar();
			sqlTransformer.setSqlFilter("sql_mssql");
			sqlTransformer.setSqlFile(file.getPath());
			sqlTransformer.setCompany("CC");
			sqlTransformer.setDbServer("ibas-dev-mssql");
			sqlTransformer.setDbPort("1433");
			sqlTransformer.setDbName("ibas_demo" + "_" + this.hashCode());
			sqlTransformer.setDbSchema("dbo");
			sqlTransformer.setDbUser("sa");
			sqlTransformer.setDbPassword("1q2w3e");
			sqlTransformer.transform();
		}
	}

	/**
	 * 测试jar包中的结构及sql创建
	 * 
	 * @throws Exception
	 */
	public void testJar() throws Exception {
		System.err.println("运行请清理test-classes目录的历史文件。");
		File folder = new File(Environment.getSqlsFolder());
		for (File file : folder.listFiles()) {
			if (file.getName().endsWith(".jar")) {
				JarTransformer jarTransformer = new JarTransformer();
				jarTransformer.setDsTemplate("ds_mssql_ibas_classic.xml");
				jarTransformer.setSqlFilter("sql_mssql");
				jarTransformer.setJarFile(file.getPath());
				jarTransformer.setCompany("CC");
				jarTransformer.setDbServer("ibas-dev-mssql");
				jarTransformer.setDbPort("1433");
				jarTransformer.setDbName("ibas_demo" + "_" + this.hashCode());
				jarTransformer.setDbSchema("dbo");
				jarTransformer.setDbUser("sa");
				jarTransformer.setDbPassword("1q2w3e");
				jarTransformer.transform();
			}
		}
	}
}
