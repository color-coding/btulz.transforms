package org.colorcoding.tools.btulz.transformers;

import java.io.File;

import org.colorcoding.tools.btulz.transformers.regions.RegionDomain;

/**
 * SQL语句的转换器
 * 
 * @author Niuren.Zhu
 *
 */
public class SqlTransformer extends DbTransformer {

	@Override
	public void transform() throws Exception {
		File dsFile = new File(this.getOutputFile());
		RegionDomain template = new RegionDomain();
		template.setTemplateFile(this.getTemplateFile());
		template.export(this.getRuntimeParameters(), dsFile);
		super.execute(dsFile);
	}
}
