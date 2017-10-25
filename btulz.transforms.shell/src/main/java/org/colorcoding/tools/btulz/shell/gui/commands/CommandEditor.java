package org.colorcoding.tools.btulz.shell.gui.commands;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.colorcoding.tools.btulz.shell.Environment;
import org.colorcoding.tools.btulz.shell.Serializer;
import org.colorcoding.tools.btulz.shell.commands.CommandBuilder;

public class CommandEditor extends JPanel {

	private static final long serialVersionUID = -4077908955463174399L;

	public CommandEditor() {
	}

	public CommandEditor(CommandBuilder builder) {
		this();
		this.setBuilder(builder);
		this.init();
	}

	private CommandBuilder builder;

	public final CommandBuilder getBuilder() {
		return builder;
	}

	public final void setBuilder(CommandBuilder builder) {
		this.builder = builder;
	}

	private String workFolder;

	public final String getWorkFolder() {
		if (this.workFolder == null) {
			this.workFolder = Environment.getCommandsFolder();
		}
		return workFolder;
	}

	CommandEditor that = this;

	private JTextArea textArea = null;
	private JTextField textField = null;

	protected void init() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		// 名称
		gridBagConstraints.gridy = 0;// 组件的纵坐标
		gridBagConstraints.gridx = 0;
		gridBagConstraints.weightx = 10.0;
		this.add(new JLabel("Name"), gridBagConstraints);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.weightx = 70.0;
		int count = 1;
		String name = this.getBuilder().getName();
		if (name.indexOf("_") > 0) {
			name = name.substring(0, name.indexOf("_"));
		}
		File folder = new File(this.getWorkFolder());
		if (folder.isDirectory() && folder.exists()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {
					if (!file.isFile()) {
						continue;
					}
					if (!file.getName().endsWith(".xml")) {
						continue;
					}
					if (!file.getName().startsWith(name)) {
						continue;
					}
					count++;
				}
			}
		}
		this.textField = new JTextField(String.format("%s_%s.xml", this.getBuilder().getName(), count));
		this.add(this.textField, gridBagConstraints);
		// 保存
		gridBagConstraints.weightx = 20.0;
		gridBagConstraints.gridx = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		JButton button = new JButton("Save");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileWriter fileWriter = null;
				try {
					if (that.textField.getText() == null || that.textField.getText().isEmpty()) {
						throw new Exception("not input name.");
					}
					fileWriter = new FileWriter(that.getWorkFolder() + File.separator + that.textField.getText());
					fileWriter.write(that.textArea.getText());
					fileWriter.flush();
					JOptionPane.showMessageDialog(that, "Successfully saved.", "Command Editor",
							JOptionPane.PLAIN_MESSAGE);
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(that, e2, "Command Editor", JOptionPane.ERROR_MESSAGE);
				} finally {
					if (fileWriter != null) {
						try {
							fileWriter.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		this.add(button, gridBagConstraints);
		// 内容
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridy = 1;// 组件的纵坐标
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		String xml = null;
		try {
			name = this.getBuilder().getName();
			this.getBuilder().setName(null);// 除去名称
			xml = Serializer.toXmlString(this.getBuilder(), true);
			this.getBuilder().setName(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.textArea = new JTextArea(xml);
		this.add(new JScrollPane(this.textArea), gridBagConstraints);
		// this.add(this.textArea, gridBagConstraints);
	}
}
