package org.colorcoding.tools.btulz.shell.command;

import java.util.EventObject;

/**
 * 命令事件
 * 
 * @author Niuren.Zhu
 *
 */
public class CommandEvent extends EventObject {

	private static final long serialVersionUID = -6766983182877236541L;

	public CommandEvent(Object source) {
		super(source);
	}

}
