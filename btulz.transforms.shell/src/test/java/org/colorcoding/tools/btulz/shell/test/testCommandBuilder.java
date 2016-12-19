package org.colorcoding.tools.btulz.shell.test;

import javax.xml.bind.JAXBException;

import org.colorcoding.tools.btulz.shell.Serializer;
import org.colorcoding.tools.btulz.shell.commands.CommandBuilder;
import org.colorcoding.tools.btulz.shell.commands.CommandItem;
import org.colorcoding.tools.btulz.shell.commands.CommandItemValue;
import org.colorcoding.tools.btulz.shell.commands.CommandItemValues;

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
		index++;
		commandItems[index].setName("is");
		commandItems[index].setDescription("");
		commandItems[index].getItemValues().setClassName(Boolean.class.getName());
		index++;
		commandItems[index].setName("and is");
		commandItems[index].setDescription("");
		commandItems[index].getItemValues().setClassName(emYesNo.class.getName());
		index++;

		commandBuilder.setItems(commandItems);
		String xml = Serializer.toXmlString(commandBuilder, true, CommandBuilder.class, CommandItem.class,
				CommandItemValue.class, CommandItemValues.class);
		System.out.println(xml);
	}
}
