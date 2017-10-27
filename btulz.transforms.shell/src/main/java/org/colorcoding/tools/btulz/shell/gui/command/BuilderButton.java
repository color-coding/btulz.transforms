package org.colorcoding.tools.btulz.shell.gui.command;

import javax.swing.JButton;

import org.colorcoding.tools.btulz.shell.command.CommandBuilder;

/**
 * 命令构造器选项卡
 * 
 * @author Niuren.Zhu
 *
 */
public class BuilderButton extends JButton {

	private static final long serialVersionUID = 4767908103256756108L;

	public BuilderButton(CommandBuilder commandBuilder) {
		this.setBuilder(commandBuilder);
		this.setName("btn_" + commandBuilder.getName());
		this.setText(commandBuilder.getName());
		if (commandBuilder.getDescription() != null && !commandBuilder.getDescription().isEmpty()) {
			this.setToolTipText(commandBuilder.getDescription());
		}
	}

	private CommandBuilder builder;

	public final CommandBuilder getBuilder() {
		return builder;
	}

	public final void setBuilder(CommandBuilder builder) {
		this.builder = builder;
	}

}
