package org.colorcoding.tools.btulz.shell.test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import javax.xml.bind.JAXBException;

import org.colorcoding.tools.btulz.shell.Serializer;
import org.colorcoding.tools.btulz.shell.command.Command;
import org.colorcoding.tools.btulz.shell.command.CommandBuilder;
import org.colorcoding.tools.btulz.shell.command.CommandItem;
import org.colorcoding.tools.btulz.shell.command.CommandListener;
import org.colorcoding.tools.btulz.shell.command.CommandManager;
import org.colorcoding.tools.btulz.shell.command.CommandMessageEvent;
import org.colorcoding.tools.btulz.shell.command.MessageType;
import org.colorcoding.tools.btulz.shell.command.TemplateGetter;
import org.colorcoding.tools.btulz.shell.command.ValidValue;
import org.colorcoding.tools.btulz.shell.command.ValidValues;

import junit.framework.TestCase;

/**
 * 命令构建器测试
 *
 * 覆盖：
 * - CommandBuilder创建与ValidValues（布尔/枚举/自定义/模板可选值）
 * - XML序列化/反序列化往返
 * - CommandManager加载命令配置并执行
 */
public class TestCommandBuilder extends TestCase {

	public enum emYesNo {
		Yes, No
	};

	/** 创建CommandBuilder并验证各种ValidValues类型与序列化往返 */
	public void testCreate() throws JAXBException {
		CommandBuilder commandBuilder = new CommandBuilder();
		commandBuilder.setName("test");
		commandBuilder.setDescription("测试");
		CommandItem commandItem;
		int index = 0;
		commandItem = commandBuilder.getItems().create();
		commandItem.setName(String.valueOf(index));
		commandItem.setContent("java");
		index++;
		commandItem = commandBuilder.getItems().create();
		commandItem.setName(String.valueOf(index));
		commandItem.setDescription("测试布尔可选值");
		commandItem.setContent("");
		commandItem.getValidValues().setClassName(Boolean.class.getName());
		commandItem.getValidValues().get();
		index++;
		commandItem = commandBuilder.getItems().create();
		commandItem.setName(String.valueOf(index));
		commandItem.setDescription("测试枚举可选值");
		commandItem.setContent("");
		commandItem.getValidValues().setClassName(emYesNo.class.getName());
		commandItem.getValidValues().get();
		index++;
		commandItem = commandBuilder.getItems().create();
		commandItem.setName(String.valueOf(index));
		commandItem.setDescription("测试自定义可选值");
		commandItem.setContent("");
		commandItem.getValidValues().add(new ValidValue("1", "first"));
		commandItem.getValidValues().add(new ValidValue("2", "tow"));
		commandItem.getValidValues().get();
		index++;
		commandItem = commandBuilder.getItems().create();
		commandItem.setName(String.valueOf(index));
		commandItem.setDescription("测试代码模板可选值");
		commandItem.setContent("");
		commandItem.getValidValues().setClassName(TemplateGetter.class.getName());
		commandItem.getValidValues().get();

		String xml = Serializer.toXmlString(commandBuilder, true, CommandBuilder.class, CommandItem.class,
				ValidValue.class, ValidValues.class);
		System.out.println(xml);

		// 反序列化验证
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		stringBuilder.append(
				"<ns2:CommandBuilder Name=\"test\" Description=\"测试\" xmlns:ns2=\"http://colorcoding.org/btulz/shell/commands\">");
		stringBuilder.append("<Item Optional=\"false\" Editable=\"true\" Name=\"is\" Description=\"\" Content=\"\">");
		stringBuilder.append("<ValidValues ClassName=\"XXXXXX\">");
		stringBuilder.append("</ValidValues>");
		stringBuilder.append("</Item>");
		stringBuilder.append("</ns2:CommandBuilder>");
		commandBuilder = (CommandBuilder) Serializer.fromXmlString(stringBuilder.toString(), CommandBuilder.class,
				CommandItem.class, ValidValue.class, ValidValues.class);
		xml = Serializer.toXmlString(commandBuilder, true, CommandBuilder.class, CommandItem.class, ValidValue.class,
				ValidValues.class);
		System.out.println(xml);
	}

	/** CommandManager加载命令配置并执行 */
	public void testCommandManager() {
		CommandManager manager = CommandManager.create();
		List<CommandBuilder> commandBuilders = manager.getCommands();
		for (CommandBuilder commandBuilder : commandBuilders) {
			System.out.println(commandBuilder.toString());
			Command command = new Command(commandBuilder);
			command.addListener(new CommandListener() {
				@Override
				public void messaged(CommandMessageEvent messageEvent) {
					if (messageEvent.getType() == MessageType.error) {
						System.err.println(messageEvent.getMessage());
					} else {
						System.out.println(messageEvent.getMessage());
					}
				}
			});
			command.run();
		}
	}

	/** 数据库命令应由一个 XML 根据 validvalues.database.xml 生成不同数据库参数。 */
	public void testDatabaseCommandBuilder() throws Exception {
		Path definitions = Files.createTempFile("btulz-validvalues-", ".xml");
		Files.writeString(definitions, "<DatabaseFeatures>"
				+ "<DatabaseType Name=\"MSSQL\"><Feature Key=\"DsTemplate\" Value=\"ds_mssql.xml\"/>"
				+ "<Feature Key=\"SqlFilter\" Value=\"sql_mssql_\"/></DatabaseType>"
				+ "<DatabaseType Name=\"PGSQL\"><Feature Key=\"DsTemplate\" Value=\"ds_pgsql.xml\"/>"
				+ "<Feature Key=\"SqlFilter\" Value=\"sql_pgsql_\"/></DatabaseType>"
				+ "</DatabaseFeatures>");
		CommandBuilder builder = new CommandBuilder();
		builder.setName("database");
		CommandItem type = builder.getItems().create();
		type.setName("DbType");
		type.setValue("MSSQL");
		type.getValidValues().setRule(ValidValues.RULE_FEATURES);
		type.getValidValues().setDefinitions(definitions.toString());
		CommandItem template = builder.getItems().create();
		template.setName("DsTemplate");
		template.setContent("-Template=${VALUE}");
		CommandItem sqlFilter = builder.getItems().create();
		sqlFilter.setName("SqlFilter");
		sqlFilter.setContent("-SqlFilter=${VALUE}");
		String[] commands = builder.toCommands();
		assertEquals(2, commands.length);
		assertEquals("-Template=ds_mssql.xml", commands[0]);
		assertEquals("-SqlFilter=sql_mssql_", commands[1]);
		assertEquals(2, type.getValidValues().size());
		builder.applyDefinition("PGSQL");
		type.setValue("PGSQL");
		assertEquals("ds_pgsql.xml", template.getValue());
		assertEquals("sql_pgsql_", sqlFilter.getValue());
		template.setValue("custom.xml");
		assertEquals("-Template=custom.xml", builder.toCommands()[0]);
		Files.deleteIfExists(definitions);
	}

	/** Shell 应直接分析独立 JAR 中的命令和同 JAR 特性资源。 */
	public void testLoadIndependentJar() throws Exception {
		Path file = Files.createTempFile("btulz-command-", ".jar");
		try (JarOutputStream output = new JarOutputStream(Files.newOutputStream(file))) {
			this.writeJarEntry(output, "commands/validvalues.test.xml",
					"<Definitions><Definition Name=\"A\"><Feature Key=\"Port\" Value=\"100\"/></Definition></Definitions>");
			this.writeJarEntry(output, "commands/sample.xml",
					"<ns2:CommandBuilder DefinitionItem=\"Type\" xmlns:ns2=\"http://colorcoding.org/btulz/shell/commands\">"
							+ "<Item Name=\"Type\" Content=\"\" Value=\"A\"><ValidValues Rule=\"features\" Definitions=\"commands/validvalues.test.xml\"/></Item>"
							+ "<Item Name=\"Port\" Content=\"-Port=${VALUE}\"/></ns2:CommandBuilder>");
		}
		CommandManager manager = new CommandManager();
		try (JarFile jarFile = new JarFile(file.toFile())) {
			manager.loadResources(jarFile);
		}
		assertEquals(1, manager.getCommands().size());
		assertEquals("-Port=100", manager.getCommands().get(0).toCommands()[0]);
		Files.deleteIfExists(file);
	}

	private void writeJarEntry(JarOutputStream output, String name, String value) throws Exception {
		output.putNextEntry(new JarEntry(name));
		output.write(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
		output.closeEntry();
	}

}
