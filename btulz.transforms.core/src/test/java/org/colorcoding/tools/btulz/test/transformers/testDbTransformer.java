package org.colorcoding.tools.btulz.test.transformers;

import java.io.File;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.transformers.DsTransformer;
import org.colorcoding.tools.btulz.transformers.SqlTransformer;

import junit.framework.TestCase;

public class testDbTransformer extends TestCase {

	private static String domain_file = testXmlTransformer.old_xml_path + File.separator + "domain_models_old.xml";

	private static String initialization_file = testXmlTransformer.old_xml_path + File.separator
			+ "ds_mssql_ibas_initialization.xml";

	public void testDS() throws Exception {
		System.err.println("运行请清理test-classes目录的历史文件。");
		DsTransformer dsTransformer = new DsTransformer();
		dsTransformer.setTemplateFile("ds_mysql_ibas_classic.xml");
		dsTransformer.addDomains(Environment.getWorkingFolder() + domain_file);
		dsTransformer.setCompany("CC");
		dsTransformer.setDbServer("ibas-dev-mysql");
		dsTransformer.setDbPort("3306");
		// dsTransformer.setDbSchema("dbo");
		dsTransformer.setDbName("ibas_demo" + "_" + dsTransformer.hashCode());
		dsTransformer.setDbUser("root");
		dsTransformer.setDbPassword("1q2w3e");
		dsTransformer.transform();
	}

	private String workspace = System.getenv("ibasWorkspace");

	public void testInitMSSQL() throws Exception {
		File file = new File(
				workspace + File.separator + "initialization" + File.separator + "ds_mssql_ibas_initialization.xml");
		if (file.exists() && file.isFile()) {
			SqlTransformer sqlTransformer = new SqlTransformer();
			sqlTransformer.setTemplateFile(file.getPath());
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
		File file = new File(
				workspace + File.separator + "initialization" + File.separator + "ds_mysql_ibas_initialization.xml");
		if (file.exists() && file.isFile()) {
			SqlTransformer sqlTransformer = new SqlTransformer();
			sqlTransformer.setTemplateFile(file.getPath());
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
		File file = new File(
				workspace + File.separator + "initialization" + File.separator + "ds_pgsql_ibas_initialization.xml");
		if (file.exists() && file.isFile()) {
			SqlTransformer sqlTransformer = new SqlTransformer();
			sqlTransformer.setTemplateFile(file.getPath());
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
		File file = new File(
				workspace + File.separator + "initialization" + File.separator + "ds_hana_ibas_initialization.xml");
		if (file.exists() && file.isFile()) {
			SqlTransformer sqlTransformer = new SqlTransformer();
			sqlTransformer.setTemplateFile(file.getPath());
			sqlTransformer.setCompany("CC");
			sqlTransformer.setDbServer("ibas-dev-hana");
			sqlTransformer.setDbPort("30015");
			sqlTransformer.setDbName("ibas_demo" + String.format("_%s", this.hashCode()));
			sqlTransformer.setDbUser("SYSTEM");
			sqlTransformer.setDbPassword("AVAtech2015!");
			sqlTransformer.transform();
		}
	}
}
