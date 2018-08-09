package org.colorcoding.tools.btulz.bobas.test.command;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.tools.btulz.bobas.Console;
import org.colorcoding.tools.btulz.bobas.command.Command4Ds;
import org.colorcoding.tools.btulz.bobas.command.Command4Init;

import junit.framework.TestCase;

public class TestCommands extends TestCase {

	public void testInit() throws MalformedURLException {
		ArrayList<String> args = new ArrayList<>();
		File folder = new File(MyConfiguration.getStartupFolder());
		folder = folder.getParentFile().getParentFile().getParentFile().getParentFile();
		String ifFolder = folder.getPath() + File.separator + "ibas.initialfantasy";
		String ibas = String.format("%1$s%2$s%3$s%2$srelease%2$s", folder.getPath(), File.separator, "ibas-framework");
		String config = String.format("%s%2$sibas.initialfantasy%2$sapp.xml", ifFolder, File.separator);
		String data = String.format("%s%2$srelease%2$sibas.initialfantasy-0.1.0.jar", ifFolder, File.separator);
		String classes = String.format("%s%2$srelease%2$sibas.initialfantasy-0.1.0.jar", ifFolder, File.separator);
		File[] files = new File(ibas).listFiles();
		if (files != null) {
			for (File item : files) {
				if (item.getName().endsWith(".jar")) {
					classes = classes + ";" + item.toURI().toURL().getPath();
				}
			}
		}
		args.add(String.format(Command4Init.COMMAND_PROMPT));
		args.add(String.format("-data=%s", data));
		args.add(String.format("-config=%s", config));
		args.add(String.format("-classes=%s", classes));
		args.add("-ignore");
		args.add("-force");
		Console.main(args.toArray(new String[] {}));
	}

	public void testDs() throws MalformedURLException {
		ArrayList<String> args = new ArrayList<>();
		File folder = new File(MyConfiguration.getStartupFolder());
		folder = folder.getParentFile().getParentFile().getParentFile().getParentFile();
		String ifFolder = folder.getPath() + File.separator + "ibas.initialfantasy";
		String config = String.format("%s%2$sibas.initialfantasy%2$sapp.xml", ifFolder, File.separator);
		String data = String.format("%s%2$srelease%2$sibas.initialfantasy-0.1.0.jar", ifFolder, File.separator);
		args.add(String.format(Command4Ds.COMMAND_PROMPT));
		args.add(String.format("-data=%s", data));
		args.add(String.format("-config=%s", config));
		args.add("-ignore");
		Console.main(args.toArray(new String[] {}));
	}
}
