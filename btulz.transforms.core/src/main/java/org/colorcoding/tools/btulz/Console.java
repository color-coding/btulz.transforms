package org.colorcoding.tools.btulz;

import org.colorcoding.tools.btulz.commands.Command4Code;
import org.colorcoding.tools.btulz.commands.Command4DSJar;
import org.colorcoding.tools.btulz.commands.Command4Ds;
import org.colorcoding.tools.btulz.commands.Command4Excel;
import org.colorcoding.tools.btulz.commands.Command4Sql;
import org.colorcoding.tools.btulz.commands.CommandsManager;

/**
 * 命令控制台
 * 
 * @author Niuren.Zhu
 *
 */
public class Console {
	private volatile static CommandsManager commandsManager;

	protected synchronized static CommandsManager getCommandsManager() {
		if (commandsManager == null) {
			synchronized (Console.class) {
				if (commandsManager == null) {
					commandsManager = new CommandsManager();
					// 注册发布的命令
					commandsManager.register(Command4Code.class);
					commandsManager.register(Command4Ds.class);
					commandsManager.register(Command4Sql.class);
					commandsManager.register(Command4DSJar.class);
					commandsManager.register(Command4Excel.class);
				}
			}
		}
		return commandsManager;
	}

	public static void main(String[] args) {
		getCommandsManager().run(args);
	}
}
