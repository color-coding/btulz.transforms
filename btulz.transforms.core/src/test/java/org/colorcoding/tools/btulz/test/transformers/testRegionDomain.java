package org.colorcoding.tools.btulz.test.transformers;

import java.io.File;
import java.util.ArrayList;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.test.models.testModels;
import org.colorcoding.tools.btulz.transformers.regions.RegionDomain;

import junit.framework.TestCase;

public class testRegionDomain extends TestCase {

	public void testRegions() throws Exception {
		String tpltFile = Environment.getResource("db").getPath();
		RegionDomain template = new RegionDomain();
		template.setTemplateFile(tpltFile + File.separator + "ds_mssql_ibas.xml");
		template.setOutPutFile(Environment.getWorkingFolder() + File.separator + "ds_mssql_ibas.out.xml");
		ArrayList<Parameter> parameters = new ArrayList<>();
		parameters.add(new Parameter("Company", "CC"));
		parameters.add(new Parameter("DbServer", "localhost"));
		parameters.add(new Parameter("DbName", "ibas_demo"));
		parameters.add(new Parameter("AppName", "btulz"));
		parameters.add(new Parameter("DbUser", "sa"));
		parameters.add(new Parameter("DbPassword", "1q2w3e"));
		parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, (new testModels()).createDomain()));
		template.export(parameters);
	}
}