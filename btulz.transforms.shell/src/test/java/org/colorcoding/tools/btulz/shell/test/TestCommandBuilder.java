package org.colorcoding.tools.btulz.shell.test;

import java.util.List;

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

}
