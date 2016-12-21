package org.colorcoding.tools.btulz.shell.gui.commands;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.colorcoding.tools.btulz.shell.commands.CommandBuilder;
import org.colorcoding.tools.btulz.shell.commands.CommandManager;

public class BuilderTab extends JPanel {

	private static final long serialVersionUID = -8890774509484515334L;

	public BuilderTab() {
		this.init();
	}

	BuilderTab that = this;

	protected void init() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		int count = 0;
		gridBagConstraints.gridy = count;// 组件的纵坐标
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 0, 0, 0);
		for (CommandBuilder commandBuilder : CommandManager.create().getCommands()) {
			BuilderButton builderButton = new BuilderButton(commandBuilder);
			builderButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					that.onBuilderClick((BuilderButton) e.getSource());
				}
			});
			count++;
			gridBagConstraints.gridy = count;// 组件的纵坐标
			this.add(builderButton, gridBagConstraints);
		}
	}

	private BuilderTabListener listener;

	public final void addBuilderTabListener(BuilderTabListener listener) {
		this.listener = listener;
	}

	public final void removeBuilderTabListener(BuilderTabListener listener) {
		this.listener = null;
	}

	public void onBuilderClick(BuilderButton button) {
		if (button != null && button.getBuilder() != null) {
			if (this.listener != null) {
				this.listener.builderSelected(button.getBuilder());
			}
		}
	}
}
