package org.colorcoding.tools.btulz.shell.gui.command;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.colorcoding.tools.btulz.shell.command.CommandBuilder;
import org.colorcoding.tools.btulz.shell.command.CommandManager;

/**
 * 命令（UI左侧）
 * 
 * @author Niuren.Zhu
 *
 */
public class BuilderTab extends JPanel {

	private static final long serialVersionUID = -8890774509484515334L;

	public BuilderTab() {
		this.init();
	}

	protected void init() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		int count = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 0.0;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		for (CommandBuilder commandBuilder : CommandManager.create().getCommands()) {
			BuilderButton builderButton = new BuilderButton(commandBuilder);
			builderButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					BuilderTab.this.onBuilderClick((BuilderButton) e.getSource());
				}
			});
			gridBagConstraints.gridy = count++;// 组件的纵坐标
			this.add(builderButton, gridBagConstraints);
		}
		// 剩余空间放在列表下方，使命令按钮始终靠顶排列。
		gridBagConstraints.gridy = count;
		gridBagConstraints.weighty = 100.0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.add(new JPanel(), gridBagConstraints);
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
