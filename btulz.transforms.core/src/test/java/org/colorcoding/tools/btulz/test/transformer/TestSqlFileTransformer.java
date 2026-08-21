package org.colorcoding.tools.btulz.test.transformer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import junit.framework.TestCase;

import org.colorcoding.tools.btulz.transformer.SqlFileTransformer;

/** SQL 转 XML 输出目标测试。 */
public class TestSqlFileTransformer extends TestCase {

	/** 单个 SQL 文件的输出目标为已存在目录时，应在目录内创建 XML 文件。 */
	public void testOutputDirectoryForSingleSqlFile() throws Exception {
		Path root = Files.createTempDirectory("btulz-sql2xml-");
		try {
			Path sqlFile = root.resolve("sample.sql");
			Path outputDirectory = Files.createDirectory(root.resolve("output"));
			Files.writeString(sqlFile, "select 1;\n");

			SqlFileTransformer transformer = new SqlFileTransformer();
			transformer.setSqlFile(sqlFile.toString());
			transformer.setOutputFile(outputDirectory.toString());
			transformer.setDbType("hana");
			transformer.transform();

			Path outputFile = outputDirectory.resolve("sql_hana_sample.xml");
			assertTrue(Files.isRegularFile(outputFile));
			String xml = Files.readString(outputFile);
			assertTrue(xml.contains("<Name>sql_hana_sample</Name>"));
			assertTrue(xml.contains("<Description>HANA SQL脚本执行编排（源文件：sample.sql）</Description>"));
			assertTrue(xml.contains("Description=\"执行 SQL 文件：sample.sql\""));
		} finally {
			Files.walk(root).sorted(Comparator.reverseOrder()).forEach(path -> {
				try {
					Files.deleteIfExists(path);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
		}
	}
}
