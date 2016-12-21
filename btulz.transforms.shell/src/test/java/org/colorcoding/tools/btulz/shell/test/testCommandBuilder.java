package org.colorcoding.tools.btulz.shell.test;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.colorcoding.tools.btulz.shell.Serializer;
import org.colorcoding.tools.btulz.shell.commands.CodeTemplateGetter;
import org.colorcoding.tools.btulz.shell.commands.Command;
import org.colorcoding.tools.btulz.shell.commands.CommandBuilder;
import org.colorcoding.tools.btulz.shell.commands.CommandItem;
import org.colorcoding.tools.btulz.shell.commands.CommandListener;
import org.colorcoding.tools.btulz.shell.commands.CommandManager;
import org.colorcoding.tools.btulz.shell.commands.CommandMessageEvent;
import org.colorcoding.tools.btulz.shell.commands.DSTemplateGetter;
import org.colorcoding.tools.btulz.shell.commands.MessageType;
import org.colorcoding.tools.btulz.shell.commands.ValidValue;
import org.colorcoding.tools.btulz.shell.commands.ValidValues;

import junit.framework.TestCase;

public class testCommandBuilder extends TestCase {

	public enum emYesNo {
		Yes, No
	};

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
		commandItem.getValidValues().setClassName(CodeTemplateGetter.class.getName());
		commandItem.getValidValues().get();
		index++;
		commandItem = commandBuilder.getItems().create();
		commandItem.setName(String.valueOf(index));
		commandItem.setDescription("测试数据结构模板可选值");
		commandItem.setContent("");
		commandItem.getValidValues().setClassName(DSTemplateGetter.class.getName());
		commandItem.getValidValues().get();
		index++;

		for (CommandItem item : commandBuilder.getItems()) {
			if (item == null) {
				continue;
			}
			System.out.print(item.getName());
			System.out.print(" ");
			System.out.print(item.getValidValues().getClassName());
			System.out.println();
			for (ValidValue validValue : item.getValidValues()) {
				System.out.println(validValue);
			}
		}

		String xml = Serializer.toXmlString(commandBuilder, true, CommandBuilder.class, CommandItem.class,
				ValidValue.class, ValidValues.class);
		System.out.println(xml);

		System.out.println();
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

	public void testCommandManager() {
		CommandManager manager = CommandManager.create();
		List<CommandBuilder> commandBuilders = manager.getCommands();
		for (CommandBuilder commandBuilder : commandBuilders) {
			System.out.println(commandBuilder.toString());
			System.out.println(commandBuilder.toCommand());
		}
		System.out.println();
		System.out.println();
		for (CommandBuilder commandBuilder : commandBuilders) {
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
			System.out.println();
		}

	}

}
