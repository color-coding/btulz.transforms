package org.colorcoding.tools.btulz.shell.commands;

/**
 * 命令监听
 * 
 * @author Niuren.Zhu
 *
 */
public interface CommandListener {

	/**
	 * 发生消息事件
	 * 
	 * @param messageEvent
	 */
	void messaged(CommandMessageEvent messageEvent);
}
