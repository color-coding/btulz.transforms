package org.colorcoding.tools.btulz.transformer;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import org.colorcoding.tools.btulz.transformer.region.RegionDomain;

/**
 * SQL语句的转换器
 * 
 * @author Niuren.Zhu
 *
 */
public class SqlTransformer extends DbTransformer {
	public static final String DEFAULT_SQL_FILTER = "sql_";

	private String sqlFile;

	public String getSqlFile() {
		return sqlFile;
	}

	public void setSqlFile(String sqlFile) {
		this.sqlFile = sqlFile;
	}

	private String sqlFilter;

	public String getSqlFilter() {
		if (this.sqlFilter == null || this.sqlFilter.isBlank()) {
			this.sqlFilter = DEFAULT_SQL_FILTER;
		}
		return sqlFilter;
	}

	public void setSqlFilter(String sqlFilter) {
		this.sqlFilter = sqlFilter == null ? null : sqlFilter.toLowerCase();
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
			if (fileName.endsWith(".xml")) {
				this.transform(sqlFile);
			}
		} else if (sqlFile.isDirectory()) {
			File[] files = sqlFile.listFiles();
			if (files != null) {
				Arrays.sort(files, Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER));
				for (File file : files) {
					String fileName = file.getName().toLowerCase();
					if (fileName.startsWith(this.getSqlFilter()) && fileName.endsWith(".xml")) {
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
		File templateFile = sqlFile;
		sqlFile = new File(this.getOutputFile(sqlFile.getName()));
		RegionDomain template = new RegionDomain();
		template.setTemplateFile(templateFile.getPath());
		template.export(this.getRuntimeParameters(), sqlFile);
		super.execute(sqlFile);
	}
}
