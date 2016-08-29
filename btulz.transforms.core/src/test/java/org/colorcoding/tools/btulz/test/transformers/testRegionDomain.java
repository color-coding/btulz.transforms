package org.colorcoding.tools.btulz.test.transformers;

import java.io.File;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.test.models.testModels;
import org.colorcoding.tools.btulz.transformers.regions.RegionDomain;

import junit.framework.TestCase;

public class testRegionDomain extends TestCase {

	public void testRegions() throws Exception {
		String tpltFile = Environment.getResource("db").getPath();
		RegionDomain template = new RegionDomain();
		template.setTemplateFile(tpltFile + File.separator + "ds_mssql_ibas.xml");
		template.setOutPutFile(Environment.getWorkingFolder() + File.separator + "ds_mssql_ibas.out.xml");
		template.addParameter(RegionDomain.REGION_DELIMITER, (new testModels()).createDomain());
		template.parse();

	}
}
