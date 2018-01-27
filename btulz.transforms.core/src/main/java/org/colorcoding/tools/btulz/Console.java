package org.colorcoding.tools.btulz;

import org.colorcoding.tools.btulz.command.Command4Code;
import org.colorcoding.tools.btulz.command.Command4DsJar;
import org.colorcoding.tools.btulz.command.Command4Ds;
import org.colorcoding.tools.btulz.command.Command4Excel;
import org.colorcoding.tools.btulz.command.Command4Sql;
import org.colorcoding.tools.btulz.command.CommandsManager;

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
					commandsManager.register(Command4DsJar.class);
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
