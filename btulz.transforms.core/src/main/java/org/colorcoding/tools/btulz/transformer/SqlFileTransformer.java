package org.colorcoding.tools.btulz.transformer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import java.nio.file.Files;

import org.colorcoding.tools.btulz.Serializer;
import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.orchestration.ISqlExecutionAction;
import org.colorcoding.tools.btulz.orchestration.ISqlExecutionActionStep;

/** 将 SQL 文件转换为现有 SQL 编排 XML，不执行数据库操作。 */
public class SqlFileTransformer extends DbTransformer {

	private String sqlFile;
	private String outputFile;
	private String dbType;
	private int statementCount = Integer.MAX_VALUE;

	public String getSqlFile() {
		return sqlFile;
	}

	public void setSqlFile(String value) {
		this.sqlFile = value;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String value) {
		this.outputFile = value;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String value) {
		this.dbType = value;
	}

	public void setStatementCount(int value) {
		this.statementCount = value > 0 ? value : Integer.MAX_VALUE;
	}


	public void transform() throws Exception {
		File source = new File(this.getSqlFile());
		if (source.isDirectory()) {
			File[] files = source.listFiles(file -> file.isFile() && file.getName().toLowerCase().endsWith(".sql"));
			if (files == null) return;
			Arrays.sort(files, Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER));
			File outputDirectory = this.outputFile == null || this.outputFile.isEmpty() ? source
					: new File(this.outputFile);
			if (outputDirectory != null) outputDirectory.mkdirs();
			for (File file : files) this.transformFile(file, outputDirectory);
			return;
		}
		if (!source.isFile()) {
			throw new Exception(String.format("sql file [%s] not exists.", this.getSqlFile()));
		}
		this.transformFile(source, this.outputFile == null || this.outputFile.isEmpty() ? source.getParentFile() : null);
	}

	private void transformFile(File source, File outputDirectory) throws Exception {
		if (this.dbType == null || this.dbType.trim().isEmpty()) {
			throw new Exception("database type is required.");
		}
		List<SqlStatement> statements = new ArrayList<>();
		int part = 1;
		try (SqlReader reader = new SqlReader(new InputStreamReader(new FileInputStream(source), StandardCharsets.UTF_8))) {
			SqlStatement statement;
			while ((statement = reader.next()) != null) {
				statements.add(statement);
				if (statements.size() >= this.statementCount) {
					this.writeXml(source, statements, part++, outputDirectory);
					statements.clear();
				}
			}
		}
		if (!statements.isEmpty()) {
			this.writeXml(source, statements, part, outputDirectory);
		}
	}

	private void writeXml(File source, List<SqlStatement> statements, int part, File outputDirectory) throws Exception {
		String resource = String.format("sql/sql_%s_template.xml", this.dbType.toLowerCase());
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
		if (input == null) {
			throw new Exception(String.format("not found database template [%s].", resource));
		}
		File output = this.createOutputFile(source, part, outputDirectory);
		StringBuilder content = new StringBuilder();
		try (InputStream stream = input) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					content.append(line).append('\n');
				}
			}
		}
		DataStructureOrchestration orchestration = (DataStructureOrchestration) Serializer.fromXmlString(
				content.toString(), DataStructureOrchestration.class);
		orchestration.getActions().clear();
		ISqlExecutionAction action = orchestration.getActions().create();
		action.setName(source.getName());
		for (SqlStatement item : statements) {
			ISqlExecutionActionStep step = action.getSteps().create();
			step.setName(String.format("line %s", item.line));
			step.setScript(item.sql);
		}
		Files.writeString(output.toPath(), Serializer.toXmlString(orchestration, true), StandardCharsets.UTF_8);
		Environment.getLogger().info(String.format("converted SQL file [%s] to XML [%s], statements [%s].",
				source.getPath(), output.getPath(), statements.size()));
	}

	private File createOutputFile(File source, int part, File outputDirectory) {
		String sourceName = source.getName();
		int sourceDot = sourceName.lastIndexOf('.');
		String sourceBase = sourceDot > 0 ? sourceName.substring(0, sourceDot) : sourceName;
		String templateName = String.format("sql_%s_%s", this.dbType.toLowerCase(), sourceBase);
		if (this.statementCount != Integer.MAX_VALUE) {
			templateName += String.format(".part%03d", part);
		}
		templateName += ".xml";
		if (outputDirectory != null) {
			File file = new File(outputDirectory, templateName);
			file.getParentFile().mkdirs();
			return file;
		}
		if (this.outputFile != null && !this.outputFile.isEmpty()) {
			File file = new File(this.outputFile);
			if (this.statementCount != Integer.MAX_VALUE) {
				String name = file.getName();
				int dot = name.lastIndexOf('.');
				name = (dot > 0 ? name.substring(0, dot) : name) + String.format(".part%03d", part) + ".xml";
				file = new File(file.getParentFile() == null ? new File(".") : file.getParentFile(), name);
			}
			if (file.getParentFile() != null) file.getParentFile().mkdirs();
			return file;
		}
		File file = new File(super.getOutputFile(templateName));
		if (file.getParentFile() != null) file.getParentFile().mkdirs();
		return file;
	}


	private static final class SqlStatement {
		private final String sql;
		private final long line;
		private SqlStatement(String sql, long line) { this.sql = sql; this.line = line; }
	}

	private static final class SqlReader implements AutoCloseable {
		private final BufferedReader reader;
		private final StringBuilder buffer = new StringBuilder();
		private long line = 1;
		private long statementLine;
		private boolean singleQuote, doubleQuote, backtickQuote, bracketQuote, lineComment, blockComment;
		private boolean escaped;
		private boolean previousStar;
		private String dollarQuote;

		private SqlReader(InputStreamReader input) { this.reader = new BufferedReader(input); }

		private SqlStatement next() throws Exception {
			int value;
			while ((value = reader.read()) >= 0) {
				char current = (char) value;
				if (current == '\n') {
					line++;
					SqlStatement batch = this.takeGoBatch();
					if (batch != null) return batch;
				}
				if (lineComment) { buffer.append(current); lineComment = current != '\n'; continue; }
				if (blockComment) {
					buffer.append(current);
					if (previousStar && current == '/') blockComment = false;
					previousStar = current == '*';
					continue;
				}
				if (dollarQuote != null) {
					buffer.append(current);
					if (current == '$' && this.readDollarEnd(dollarQuote)) {
						buffer.append(dollarQuote.substring(1));
						dollarQuote = null;
					}
					continue;
				}
				if (singleQuote || doubleQuote || backtickQuote || bracketQuote) {
					buffer.append(current);
					if (escaped) {
						escaped = false;
						continue;
					}
					if (current == '\\' && !bracketQuote) {
						escaped = true;
						continue;
					}
					char quote = singleQuote ? '\'' : doubleQuote ? '"' : backtickQuote ? '`' : ']';
					if (current == quote) {
						reader.mark(1);
						int next = reader.read();
						if (next == quote) buffer.append((char) next);
						else {
							if (next >= 0) reader.reset();
							singleQuote = false; doubleQuote = false; backtickQuote = false; bracketQuote = false;
						}
					}
					continue;
				}
				if (current == '-' || current == '/') {
					reader.mark(1);
					int next = reader.read();
					if (current == '-' && next == '-') {
						if (buffer.toString().trim().isEmpty()) statementLine = line;
						buffer.append("--"); lineComment = true; continue;
					}
					if (current == '/' && next == '*') {
						if (buffer.toString().trim().isEmpty()) statementLine = line;
						buffer.append("/*"); blockComment = true; continue;
					}
					if (next >= 0) reader.reset();
				}
				if (current == '#') {
					if (buffer.toString().trim().isEmpty()) statementLine = line;
					buffer.append(current); lineComment = true; continue;
				}
				if (current == '\'') singleQuote = true;
				if (current == '"') doubleQuote = true;
				if (current == '`') backtickQuote = true;
				if (current == '[') bracketQuote = true;
				if (current == '$') {
					String tag = this.readDollarStart();
					if (tag != null) {
						if (buffer.toString().trim().isEmpty()) statementLine = line;
						buffer.append(tag); dollarQuote = tag; continue;
					}
				}
				if (current == ';') {
					String sql = buffer.toString().trim();
					buffer.setLength(0);
					if (!sql.isEmpty()) { SqlStatement result = new SqlStatement(sql, statementLine); statementLine = 0; return result; }
				}
				if (statementLine == 0 && !Character.isWhitespace(current)) statementLine = line;
				buffer.append(current);
			}
			if (singleQuote || doubleQuote || backtickQuote || bracketQuote || dollarQuote != null || blockComment) {
				throw new Exception(String.format("unclosed SQL quote or comment at line %s.", line));
			}
			String sql = buffer.toString().trim();
			buffer.setLength(0);
			return sql.isEmpty() ? null : new SqlStatement(sql, statementLine);
		}

		private String readDollarStart() throws Exception {
			reader.mark(256);
			StringBuilder tag = new StringBuilder("$");
			int value;
			while ((value = reader.read()) >= 0) {
				char current = (char) value;
				if (current == '$') return tag.append('$').toString();
				if (!(Character.isLetterOrDigit(current) || current == '_') || tag.length() > 128) break;
				tag.append(current);
			}
			reader.reset();
			return null;
		}

		/** 支持 SQL Server/Sybase 常见的独立 GO 批次分隔符。 */
		private SqlStatement takeGoBatch() {
			String text = buffer.toString();
			int newline = text.lastIndexOf('\n');
			String lastLine = text.substring(newline + 1).trim();
			if (!"GO".equalsIgnoreCase(lastLine)) return null;
			String sql = text.substring(0, newline).trim();
			buffer.setLength(0);
			if (sql.isEmpty()) return null;
			SqlStatement result = new SqlStatement(sql, statementLine);
			statementLine = 0;
			return result;
		}

		private boolean readDollarEnd(String tag) throws Exception {
			reader.mark(tag.length());
			StringBuilder end = new StringBuilder("$");
			for (int i = 1; i < tag.length(); i++) {
				int value = reader.read();
				if (value < 0) { reader.reset(); return false; }
				end.append((char) value);
			}
			if (tag.equals(end.toString())) return true;
			reader.reset();
			return false;
		}

		@Override public void close() throws Exception { reader.close(); }
	}
}
