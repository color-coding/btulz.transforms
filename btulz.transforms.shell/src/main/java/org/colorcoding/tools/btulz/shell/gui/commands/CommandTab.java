package org.colorcoding.tools.btulz.shell.gui.commands;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.colorcoding.tools.btulz.shell.commands.CommandBuilder;
import org.colorcoding.tools.btulz.shell.commands.CommandItem;
import org.colorcoding.tools.btulz.shell.commands.ValidValue;

/**
 * 命令执行选项卡
 * 
 * @author Niuren.Zhu
 *
 */
public class CommandTab extends WorkingTab {

	private static final long serialVersionUID = -1962365855779611380L;

	public CommandTab(CommandBuilder commandBuilder) {
		this.setBuilder(commandBuilder);
		this.init();
	}

	private CommandBuilder builder;

	public final CommandBuilder getBuilder() {
		return builder;
	}

	public final void setBuilder(CommandBuilder builder) {
		this.builder = builder;
	}

	protected void init() {
		int itemCount = 2 + this.getBuilder().getItems().getCount();
		this.setLayout(new GridLayout(itemCount, 3, 2, 2));
		// 名称
		this.add(new JLabel(this.getBuilder().getName()));
		this.add(new JLabel(this.getBuilder().getDescription()));
		this.add(new JButton("run"));
		// 内容
		this.add(new JLabel());
		this.add(new JLabel());
		this.add(new JButton("cancel"));
		// 内容项目
		for (CommandItem commandItem : this.getBuilder().getItems()) {
			this.addCommandItemLine(commandItem, 0);
		}
	}

	private void addCommandItemLine(CommandItem commandItem, int indent) {
		// 添加命令内容
		JTextField textField = new JTextField();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			stringBuilder.append(" ");
			stringBuilder.append(" ");
		}
		stringBuilder.append(commandItem.getContent());
		textField.setText(stringBuilder.toString());
		textField.setEditable(commandItem.isEditable());
		textField.setToolTipText(commandItem.getDescription());
		this.add(textField);
		// 添加命令值
		JTextField textValue = null;
		commandItem.getValidValues().get();
		if (commandItem.getValidValues().size() > 0) {
			JComboBox<?> comboBox = new JComboBox<ValidValue>(commandItem.getValidValues().toArray());
			comboBox.setEditable(false);
			comboBox.setSelectedIndex(0);
			this.add(comboBox);
		} else {
			textValue = new JTextField(commandItem.getValue());
			this.add(textValue);
		}
		// 添加额外内容
		if (commandItem.getValidValues().getClassName() != null
				&& commandItem.getValidValues().getClassName().equals(JFileChooser.class.getName())) {
			JButton button = new JButton("...");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser jfc = new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					jfc.showDialog(new JLabel(), "选择");
					if (button != null && jfc.getSelectedFile() != null) {
						button.setText(jfc.getSelectedFile().getPath());
					}
				}
			});
			this.add(button);
		} else {
			JButton button = new JButton("...");
			button.setEnabled(false);
			this.add(button);
		}
		// 添加子项
		for (CommandItem item : commandItem.getItems()) {
			this.addCommandItemLine(item, indent + 1);
		}
	}
}
