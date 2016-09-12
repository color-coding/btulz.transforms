package org.colorcoding.tools.btulz.transformers;

import java.io.File;

/**
 * 转换jar包中的数据结构和初始化语句
 * 
 * @author Niuren.Zhu
 *
 */
public class JarTransformer extends DbTransformer {

	private String sqlFilter;

	public String getSqlFilter() {
		return sqlFilter;
	}

	public void setSqlFilter(String sqlFilter) {
		this.sqlFilter = sqlFilter;
	}

	private String dsTemplate;

	public String getDsTemplate() {
		return dsTemplate;
	}

	public void setDsTemplate(String dsTemplate) {
		this.dsTemplate = dsTemplate;
	}

	private String jarFile;

	public String getJarFile() {
		return jarFile;
	}

	public void setJarFile(String jarFile) {
		this.jarFile = jarFile;
	}

	@Override
	public void transform() throws Exception {
		File file = new File(this.getJarFile());
		if (!file.isFile() || !file.exists()) {
			return;
		}
		// 首先创建数据结构
		DsTransformer4Jar dsTransformer = new DsTransformer4Jar();
		dsTransformer.setTemplateFile(this.getDsTemplate());
		dsTransformer.addDomains(file);
		dsTransformer.setCompany(this.getCompany());
		dsTransformer.setDbServer(this.getDbServer());
		dsTransformer.setDbPort(this.getDbPort());
		dsTransformer.setDbSchema(this.getDbSchema());
		dsTransformer.setDbName(this.getDbName());
		dsTransformer.setDbUser(this.getDbUser());
		dsTransformer.setDbPassword(this.getDbPassword());
		dsTransformer.transform();
		// 执行初始化语句
		SqlTransformer4Jar sqlTransformer = new SqlTransformer4Jar();
		sqlTransformer.setSqlFile(this.getJarFile());
		sqlTransformer.setSqlFilter(this.getSqlFilter());
		sqlTransformer.setCompany(this.getCompany());
		sqlTransformer.setDbServer(this.getDbServer());
		sqlTransformer.setDbPort(this.getDbPort());
		sqlTransformer.setDbSchema(this.getDbSchema());
		sqlTransformer.setDbName(this.getDbName());
		sqlTransformer.setDbUser(this.getDbUser());
		sqlTransformer.setDbPassword(this.getDbPassword());
		sqlTransformer.transform();
	}
}
