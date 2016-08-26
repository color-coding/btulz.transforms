package org.colorcoding.tools.btulz.test.templates;

import java.io.File;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.templates.RegionTemplate;

import junit.framework.TestCase;

public class testTemplate extends TestCase {

	public void testOutput() throws Exception {
		String tpltFile = Environment.getResource("db").getPath();
		RegionTemplate template = new RegionTemplate();
		template.setTemplateFile(tpltFile + File.separator + "ds_mssql_ibas.xml");
		template.setOutPutFile(Environment.getWorkingFolder() + File.separator + "ds_mssql_ibas.out.xml");
		template.parse();

	}
}
