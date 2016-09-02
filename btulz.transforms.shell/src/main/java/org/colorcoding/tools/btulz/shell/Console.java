package org.colorcoding.tools.btulz.shell;

import java.util.ArrayList;

import org.colorcoding.tools.btulz.shell.command.CommandConsole;
import org.colorcoding.tools.btulz.shell.gui.GuiConsole;

public class Console {
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			// 没有参数则启动ui
			GuiConsole.main(args);
			return;
		}
		ArrayList<String> commands = new ArrayList<>();
		StringBuilder commond = null;
		for (String arg : args) {
			if (arg == null) {
				continue;
			}
			if (arg.startsWith("--")) {
				if (commond != null && commond.length() > 0) {
					commands.add(commond.toString());
				}
				commond = new StringBuilder();
			}
			commond.append(arg);
		}
		// 记录最后一条
		if (commond != null && commond.length() > 0) {
			commands.add(commond.toString());
		}
		CommandConsole.main(commands.toArray(new String[] {}));
	}
}
