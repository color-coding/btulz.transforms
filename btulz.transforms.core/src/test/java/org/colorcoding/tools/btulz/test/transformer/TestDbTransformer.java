package org.colorcoding.tools.btulz.test.transformer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.colorcoding.tools.btulz.test.Environment;
import org.colorcoding.tools.btulz.transformer.DsTransformer;
import org.colorcoding.tools.btulz.transformer.JarTransformer;
import org.colorcoding.tools.btulz.transformer.SqlTransformer;

import junit.framework.TestCase;

/**
 * 数据库转换器测试
 *
 * 覆盖： - DsTransformer：数据结构创建（SQLite为代表） - SqlTransformer：SQL初始化脚本执行（MSSQL为代表） -
 * JarTransformer：JAR包中结构及SQL的创建
 *
 * 注意：MYSQL/PGSQL/HANA等与MSSQL/SQLite流程一致，仅连接参数不同； 多数测试依赖外部数据库环境，文件存在性检查后执行
 */
public class TestDbTransformer extends TestCase {

	/** 目录模式只处理 SQL 前缀匹配的 XML，不应读取 Domain XML。 */
	public void testSqlDirectoryFilter() throws Exception {
		Path folder = Files.createTempDirectory("btulz-sql-filter-");
		Path domain = Files.createFile(folder.resolve("ds_initialfantasy.xml"));
		Path mysql = Files.createFile(folder.resolve("sql_mysql_initialization.xml"));
		Path pgsql = Files.createFile(folder.resolve("sql_pgsql_initialization.xml"));
		Path misplaced = Files.createFile(folder.resolve("backup_sql_mysql_initialization.xml"));
		try {
			CollectingSqlTransformer transformer = new CollectingSqlTransformer();
			transformer.setSqlFile(folder.toString());
			transformer.setSqlFilter("sql_mysql_");
			transformer.transform();
			assertEquals(1, transformer.files.size());
			assertEquals(mysql.toFile(), transformer.files.get(0));

			transformer = new CollectingSqlTransformer();
			transformer.setSqlFile(folder.toString());
			transformer.transform();
			assertEquals(2, transformer.files.size());
		} finally {
			Files.deleteIfExists(domain);
			Files.deleteIfExists(mysql);
			Files.deleteIfExists(pgsql);
			Files.deleteIfExists(misplaced);
			Files.deleteIfExists(folder);
		}
	}

	private static class CollectingSqlTransformer extends SqlTransformer {
		private final List<File> files = new ArrayList<>();

		@Override
		public void transform(File file) {
			this.files.add(file);
		}
	}

	/** 数据结构创建（SQLite为代表） */
	public void testDS() throws Exception {
		System.err.println("运行请清理test-classes目录的历史文件。");
		DsTransformer dsTransformer = new DsTransformer();
		dsTransformer.setTemplateFile("ds_sqlite_ibas_classic.xml");
		dsTransformer.addDomains(Environment.getWorkingFolder() + File.separator + Environment.getXmlModelsFileOld());
		dsTransformer.setCompany("CC");
		dsTransformer.setDbServer("ibas-db-sybase");
		dsTransformer.setDbPort("2638");
		dsTransformer.setDbSchema("dbo");
		dsTransformer.setDbName("ibas_demo.db");
		dsTransformer.setDbUser("dba");
		dsTransformer.setDbPassword("1q2w3e");
		dsTransformer.transform();
	}

	/** SQL初始化脚本执行（MSSQL为代表） */
	public void testInitSQL() throws Exception {
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

	/** JAR包中的结构及SQL创建 */
	public void testJar() throws Exception {
		System.err.println("运行请清理test-classes目录的历史文件。");
		File folder = new File(Environment.getSqlsFolder());
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
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
}
