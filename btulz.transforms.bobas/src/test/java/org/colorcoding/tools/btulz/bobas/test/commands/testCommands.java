package org.colorcoding.tools.btulz.bobas.test.commands;

import java.io.File;
import java.util.ArrayList;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.tools.btulz.bobas.commands.Command4Init;

import junit.framework.TestCase;

public class testCommands extends TestCase {

	public void testInit() {
		ArrayList<String> args = new ArrayList<>();
		args.add(String.format(Command4Init.COMMAND_PROMPT)); // 命令
		args.add(String.format("-config=%s", MyConfiguration.getWorkFolder() + File.separator + "app.xml"));

	}
}
