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

	public void testSQL() throws Exception {
		SqlTransformer sqlTransformer = new SqlTransformer();
		sqlTransformer.setTemplateFile(Environment.getWorkingFolder() + initialization_file);
		sqlTransformer.setCompany("CC");
		sqlTransformer.setDbServer("ibas-dev-mssql");
		sqlTransformer.setDbPort("1433");
		sqlTransformer.setDbSchema("dbo");
		sqlTransformer.setDbName("ibas_demo");
		sqlTransformer.setDbSchema("dbo");
		sqlTransformer.setDbUser("sa");
		sqlTransformer.setDbPassword("1q2w3e");
		sqlTransformer.transform();
	}
}
