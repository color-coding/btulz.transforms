package org.colorcoding.tools.btulz.bobas.test.commands;

import java.io.File;
import java.util.ArrayList;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.tools.btulz.bobas.Console;
import org.colorcoding.tools.btulz.bobas.commands.Command4Init;

import junit.framework.TestCase;

public class testCommands extends TestCase {

	public void testInit() {
		File folder = new File(MyConfiguration.getStartupFolder());
		folder = folder.getParentFile().getParentFile().getParentFile().getParentFile();
		String ifFolder = folder.getPath() + File.separator + "ibas.initialfantasy";
		String config = String.format("%s%sibas.initialfantasy%sapp.xml", ifFolder, File.separator, File.separator);
		String data = String.format("%s%srelease%sibas.initialfantasy-0.0.1.jar", ifFolder, File.separator,
				File.separator);
		ArrayList<String> args = new ArrayList<>();
		args.add(String.format(Command4Init.COMMAND_PROMPT)); // 命令
		args.add(String.format("-data=%s", data));
		args.add(String.format("-config=%s", config));
		Console.main(args.toArray(new String[] {}));
		data = String.format("%s%sibas.initialfantasy%starget%sclasses", ifFolder, File.separator, File.separator,
				File.separator);
		args = new ArrayList<>();
		args.add(String.format(Command4Init.COMMAND_PROMPT)); // 命令
		args.add(String.format("-data=%s", data));
		args.add(String.format("-config=%s", config));
		Console.main(args.toArray(new String[] {}));
	}
}
