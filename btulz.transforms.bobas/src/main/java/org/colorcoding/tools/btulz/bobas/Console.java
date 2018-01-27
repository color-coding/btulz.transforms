package org.colorcoding.tools.btulz.bobas;

import org.colorcoding.tools.btulz.bobas.command.Command4Ds;
import org.colorcoding.tools.btulz.bobas.command.Command4Init;
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
					commandsManager.register(Command4Init.class);
					commandsManager.register(Command4Ds.class);
				}
			}
		}
		return commandsManager;
	}

	public static void main(String[] args) {
		getCommandsManager().run(args);
	}
}
