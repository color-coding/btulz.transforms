package org.colorcoding.tools.btulz.shell.commands;

/**
 * 命令消息事件
 * 
 * @author Niuren.Zhu
 *
 */
public class CommandMessageEvent extends CommandEvent {

	private static final long serialVersionUID = 4889984933753120812L;

	public CommandMessageEvent(Object source) {
		super(source);
		this.setType(MessageType.common);
	}

	public CommandMessageEvent(Object source, String message) {
		this(source);
		this.setMessage(message);
	}

	public CommandMessageEvent(Object source, MessageType type, String message) {
		this(source);
		this.setType(type);
		this.setMessage(message);
	}

	private MessageType type;

	public final MessageType getType() {
		return type;
	}

	public final void setType(MessageType type) {
		this.type = type;
	}

	private String message;

	public final String getMessage() {
		return message;
	}

	private final void setMessage(String message) {
		this.message = message;
	}
}
