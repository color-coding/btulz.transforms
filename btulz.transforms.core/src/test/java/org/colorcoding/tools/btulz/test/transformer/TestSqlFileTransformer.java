package org.colorcoding.tools.btulz.test.transformer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
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
			Files.write(sqlFile, "select 1;\n".getBytes(StandardCharsets.UTF_8));

			SqlFileTransformer transformer = new SqlFileTransformer();
			transformer.setSqlFile(sqlFile.toString());
			transformer.setOutputFile(outputDirectory.toString());
			transformer.setDbType("hana");
			transformer.transform();

			Path outputFile = outputDirectory.resolve("sql_hana_sample.xml");
			assertTrue(Files.isRegularFile(outputFile));
			String xml = new String(Files.readAllBytes(outputFile), StandardCharsets.UTF_8);
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

	/** HANA 中的反斜杠是字符串内容，不应阻止后续 SQL 语句分割。 */
	public void testHanaBackslashDoesNotMergeStatements() throws Exception {
		Path root = Files.createTempDirectory("btulz-sql2xml-hana-");
		try {
			Path sqlFile = root.resolve("backslash.sql");
			Path outputFile = root.resolve("backslash.xml");
			Files.write(sqlFile, "INSERT INTO T VALUES (N'\\');\nINSERT INTO T VALUES (N'next');\n"
					.getBytes(StandardCharsets.UTF_8));
			SqlFileTransformer transformer = new SqlFileTransformer();
			transformer.setSqlFile(sqlFile.toString());
			transformer.setOutputFile(outputFile.toString());
			transformer.setDbType("hana");
			transformer.transform();
			String xml = new String(Files.readAllBytes(outputFile), StandardCharsets.UTF_8);
			assertEquals(2, count(xml, "<Script>"));
			assertTrue(xml.contains("N'\\'"));
			assertTrue(xml.contains("N'next'"));
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

	private static int count(String value, String search) {
		int count = 0;
		for (int offset = 0; (offset = value.indexOf(search, offset)) >= 0; offset += search.length()) {
			count++;
		}
		return count;
	}

	/** GO 批次分隔符的边界场景：首行 GO 不崩溃；末尾无换行的 GO 不生成语句。 */
	public void testGoBatchEdges() throws Exception {
		Path root = Files.createTempDirectory("btulz-sql2xml-go-");
		try {
			// GO 为首行（前序语句为空）
			Path leadGo = root.resolve("lead.sql");
			Files.write(leadGo, "GO\nselect 1;\n".getBytes(StandardCharsets.UTF_8));
			SqlFileTransformer transformer = new SqlFileTransformer();
			transformer.setSqlFile(leadGo.toString());
			transformer.setDbType("mysql");
			transformer.setOutputFile(root.resolve("lead.xml").toString());
			transformer.transform();
			String xml = new String(Files.readAllBytes(root.resolve("lead.xml")), StandardCharsets.UTF_8);
			assertFalse(xml.contains("<Script>GO</Script>"));
			assertTrue(xml.contains("<Script>select 1</Script>"));

			// 分号语句后紧跟 GO（缓冲首行即 GO）
			Path mixedGo = root.resolve("mixed.sql");
			Files.write(mixedGo, "select 1;\nGO\nselect 2;\n".getBytes(StandardCharsets.UTF_8));
			transformer = new SqlFileTransformer();
			transformer.setSqlFile(mixedGo.toString());
			transformer.setDbType("mysql");
			transformer.setOutputFile(root.resolve("mixed.xml").toString());
			transformer.transform();
			xml = new String(Files.readAllBytes(root.resolve("mixed.xml")), StandardCharsets.UTF_8);
			assertTrue(xml.contains("<Script>select 1</Script>"));
			assertTrue(xml.contains("<Script>select 2</Script>"));
			assertFalse(xml.contains("<Script>GO</Script>"));

			// 文件末尾 GO 无换行符
			Path tailGo = root.resolve("tail.sql");
			Files.write(tailGo, "select 1;\nGO".getBytes(StandardCharsets.UTF_8));
			transformer = new SqlFileTransformer();
			transformer.setSqlFile(tailGo.toString());
			transformer.setDbType("mysql");
			transformer.setOutputFile(root.resolve("tail.xml").toString());
			transformer.transform();
			xml = new String(Files.readAllBytes(root.resolve("tail.xml")), StandardCharsets.UTF_8);
			assertTrue(xml.contains("<Script>select 1</Script>"));
			assertFalse(xml.contains("<Script>GO</Script>"));
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
