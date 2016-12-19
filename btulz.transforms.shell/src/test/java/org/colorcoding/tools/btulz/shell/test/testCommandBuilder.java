package org.colorcoding.tools.btulz.shell.test;

import javax.xml.bind.JAXBException;

import org.colorcoding.tools.btulz.shell.Serializer;
import org.colorcoding.tools.btulz.shell.commands.CommandBuilder;
import org.colorcoding.tools.btulz.shell.commands.CommandItem;
import org.colorcoding.tools.btulz.shell.commands.CommandManager;
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
		commandItems[index].setName("java");
		commandItems[index].setDescription("");
		commandItems[index].setContent("");
		index++;
		commandItems[index] = new CommandItem();
		commandItems[index].setName("is");
		commandItems[index].setDescription("");
		commandItems[index].setContent("");
		commandItems[index].getValidValues().setClassName(Boolean.class.getName());
		commandItems[index].getValidValues().get();
		index++;
		commandItems[index] = new CommandItem();
		commandItems[index].setName("and is");
		commandItems[index].setDescription("");
		commandItems[index].setContent("");
		commandItems[index].getValidValues().setClassName(emYesNo.class.getName());
		commandItems[index].getValidValues().get();
		index++;

		commandBuilder.setItems(commandItems);
		String xml = Serializer.toXmlString(commandBuilder, true, CommandBuilder.class, CommandItem.class,
				ValidValue.class, ValidValues.class);
		System.out.println(xml);
	}

	public void testPrefabricated() {
		CommandManager manager = CommandManager.create();
		for (CommandBuilder commandBuilder : manager.getCommands()) {
			System.out.print(commandBuilder.toString());
			System.out.print(" ");
			System.out.println(commandBuilder.toCommand());
		}
	}
}
