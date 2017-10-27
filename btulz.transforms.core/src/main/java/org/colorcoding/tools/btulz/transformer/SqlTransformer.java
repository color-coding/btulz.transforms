package org.colorcoding.tools.btulz.transformer;

import java.io.File;

import org.colorcoding.tools.btulz.transformer.region.RegionDomain;

/**
 * SQL语句的转换器
 * 
 * @author Niuren.Zhu
 *
 */
public class SqlTransformer extends DbTransformer {

	private String sqlFile;

	public String getSqlFile() {
		return sqlFile;
	}

	public void setSqlFile(String sqlFile) {
		this.sqlFile = sqlFile;
	}

	private String sqlFilter;

	public String getSqlFilter() {
		if (this.sqlFilter == null) {
			this.sqlFilter = "";
		}
		return sqlFilter;
	}

	public void setSqlFilter(String sqlFilter) {
		this.sqlFilter = sqlFilter;
	}

	/**
	 * 获取输出文件名称
	 * 
	 * @return
	 */
	protected String getOutputFile() {
		File file = new File(this.getSqlFile());
		return this.getOutputFile(file.isFile() ? file.getName() : this.getSqlFile());
	}

	@Override
	public void transform() throws Exception {
		File sqlFile = new File(this.getSqlFile());
		if (sqlFile.isFile()) {
			String fileName = sqlFile.getName().toLowerCase();
			if (fileName.indexOf(this.getSqlFilter()) >= 0 && fileName.endsWith(".xml")) {
				this.transform(sqlFile);
			}
		} else if (sqlFile.isDirectory()) {
			File[] files = sqlFile.listFiles();
			if (files != null) {
				for (File file : files) {
					String fileName = file.getName().toLowerCase();
					if (fileName.indexOf(this.getSqlFilter()) >= 0 && fileName.endsWith(".xml")) {
						this.transform(file);
					}
				}
			}
		}
	}

	public void transform(File sqlFile) throws Exception {
		if (!sqlFile.exists() || !sqlFile.isFile()) {
			throw new Exception(String.format("sql file [%s] not exists.", this.getSqlFile()));
		}
		sqlFile = new File(this.getOutputFile(sqlFile.getName()));
		RegionDomain template = new RegionDomain();
		template.setTemplateFile(this.getSqlFile());
		template.export(this.getRuntimeParameters(), sqlFile);
		super.execute(sqlFile);
	}
}
