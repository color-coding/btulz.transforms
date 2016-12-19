package org.colorcoding.tools.btulz.shell.commands;

/**
 * 命令事件支持
 * 
 * @author Niuren.Zhu
 *
 */
public class CommandEventSupport {

	public CommandEventSupport(Object source) {
		if (source == null) {
			throw new NullPointerException();
		}
		this.source = source;
	}

	private static final int array_extension_step = 2;
	private Object source = null;
	private CommandListener[] listeners;

	public void addListener(CommandListener listener) {
		if (listener == null) {
			return;
		}
		if (this.listeners == null) {
			this.listeners = new CommandListener[array_extension_step];
		}
		boolean done = false;
		// 检查是否已监听
		for (int i = 0; i < this.listeners.length; i++) {
			if (this.listeners[i] == listener) {
				done = true;
				break;
			}
		}
		if (!done) {
			// 没有监听
			done = false;
			for (int i = 0; i < this.listeners.length; i++) {
				if (this.listeners[i] == null) {
					this.listeners[i] = listener;
					done = true;
					break;
				}
			}
		}
		if (!done) {
			// 数组不够
			CommandListener[] tmps = new CommandListener[this.listeners.length + array_extension_step];
			int i = 0;
			for (; i < this.listeners.length; i++) {
				tmps[i] = this.listeners[i];
			}
			tmps[i] = listener;
			this.listeners = tmps;
		}
	}

	public void removeListener(CommandListener listener) {
		if (listener == null) {
			return;
		}
		if (this.listeners == null) {
			return;
		}
		for (int i = 0; i < this.listeners.length; i++) {
			if (this.listeners[i] == listener) {
				this.listeners[i] = null;
			}
		}
	}

	public void fireMessages(MessageType type, String message) {
		if (this.listeners == null) {
			return;
		}
		for (CommandListener item : this.listeners) {
			if (item == null) {
				continue;
			}
			item.messaged(new CommandMessageEvent(this.source, type, message));
		}
	}

}
