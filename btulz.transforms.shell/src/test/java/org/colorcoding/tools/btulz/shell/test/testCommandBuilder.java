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
		CommandItem[] commandItems = new CommandItem[99];
		int index = 0;
		commandItems[index] = new CommandItem();
		commandItems[index].setName(String.valueOf(index));
		commandItems[index].setContent("java");
		index++;
		commandItems[index] = new CommandItem();
		commandItems[index].setName(String.valueOf(index));
		commandItems[index].setDescription("测试布尔可选值");
		commandItems[index].setContent("");
		commandItems[index].getValidValues().setClassName(Boolean.class.getName());
		commandItems[index].getValidValues().get();
		index++;
		commandItems[index] = new CommandItem();
		commandItems[index].setName(String.valueOf(index));
		commandItems[index].setDescription("测试枚举可选值");
		commandItems[index].setContent("");
		commandItems[index].getValidValues().setClassName(emYesNo.class.getName());
		commandItems[index].getValidValues().get();
		index++;
		commandItems[index] = new CommandItem();
		commandItems[index].setName(String.valueOf(index));
		commandItems[index].setDescription("测试自定义可选值");
		commandItems[index].setContent("");
		commandItems[index].getValidValues().add(new ValidValue("1", "first"));
		commandItems[index].getValidValues().add(new ValidValue("2", "tow"));
		commandItems[index].getValidValues().get();
		index++;
		commandItems[index] = new CommandItem();
		commandItems[index].setName(String.valueOf(index));
		commandItems[index].setDescription("测试代码模板可选值");
		commandItems[index].setContent("");
		commandItems[index].getValidValues().setClassName(CodeTemplateGetter.class.getName());
		commandItems[index].getValidValues().get();
		index++;
		commandItems[index] = new CommandItem();
		commandItems[index].setName(String.valueOf(index));
		commandItems[index].setDescription("测试数据结构模板可选值");
		commandItems[index].setContent("");
		commandItems[index].getValidValues().setClassName(DSTemplateGetter.class.getName());
		commandItems[index].getValidValues().get();
		index++;

		commandBuilder.setItems(commandItems);

		for (CommandItem commandItem : commandItems) {
			if (commandItem == null) {
				continue;
			}
			System.out.print(commandItem.getName());
			System.out.print(" ");
			System.out.print(commandItem.getValidValues().getClassName());
			System.out.println();
			for (ValidValue validValue : commandItem.getValidValues()) {
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

	public void testPrefabricated() {
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
