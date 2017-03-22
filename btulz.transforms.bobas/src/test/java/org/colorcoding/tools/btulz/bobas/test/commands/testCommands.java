package org.colorcoding.tools.btulz.bobas.test.commands;

import java.io.File;
import java.util.ArrayList;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.tools.btulz.bobas.Console;
import org.colorcoding.tools.btulz.bobas.commands.Command4init;

import junit.framework.TestCase;

public class testCommands extends TestCase {

	public void testInit() {
		File folder = new File(MyConfiguration.getStartupFolder());
		folder = folder.getParentFile().getParentFile().getParentFile().getParentFile();
		String ifFolder = folder.getPath() + File.separator + "ibas.initialfantasy";
		String ibas = String.format("%1$s%2$s%3$s%2$srelease%2$sbobas.businessobjectscommon-0.1.2.jar",
				folder.getPath(), File.separator, "ibas-framework");
		String config = String.format("%s%2$sibas.initialfantasy%2$sapp.xml", ifFolder, File.separator);
		String data = String.format("%s%2$srelease%2$sibas.initialfantasy-0.0.1.jar", ifFolder, File.separator);
		String classes = String.format("%s%2$srelease%2$sibas.initialfantasy-0.0.1.jar", ifFolder, File.separator);
		classes = classes + ";";// + ibas;
		ArrayList<String> args = new ArrayList<>();
		args.add(String.format(Command4init.COMMAND_PROMPT)); // 命令
		args.add(String.format("-data=%s", data));
		args.add(String.format("-config=%s", config));
		args.add(String.format("-classes=%s", classes));
		// Console.main(args.toArray(new String[] {}));
		data = String.format("%s%2$sibas.initialfantasy%2$starget%2$sclasses%2$sinitialization", ifFolder,
				File.separator);
		classes = MyConfiguration.getStartupFolder();
		classes = String.format("%s%2$sibas.initialfantasy%2$starget%2$sclasses", ifFolder, File.separator);
		classes = classes + ";";// + ibas;
		args = new ArrayList<>();
		args.add(String.format(Command4init.COMMAND_PROMPT)); // 命令
		args.add(String.format("-data=%s", data));
		args.add(String.format("-config=%s", config));
		args.add(String.format("-classes=%s", classes));
		Console.main(args.toArray(new String[] {}));
	}
}
