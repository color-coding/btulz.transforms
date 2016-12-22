package org.colorcoding.tools.btulz.shell.gui.commands;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.colorcoding.tools.btulz.shell.Environment;
import org.colorcoding.tools.btulz.shell.Serializer;
import org.colorcoding.tools.btulz.shell.commands.Command;
import org.colorcoding.tools.btulz.shell.commands.CommandBuilder;
import org.colorcoding.tools.btulz.shell.commands.CommandItem;
import org.colorcoding.tools.btulz.shell.commands.CommandListener;
import org.colorcoding.tools.btulz.shell.commands.CommandMessageEvent;

/**
 * 命令执行选项卡
 * 
 * @author Niuren.Zhu
 *
 */
public class CommandTab extends WorkingTab {

	private static final long serialVersionUID = -1962365855779611380L;
	/**
	 * 工作层
	 */
	public static final String PANEL_COMMAND = "command_panel";

	public CommandTab(CommandBuilder commandBuilder) {
		super(commandBuilder.getName());
		this.setBuilder(commandBuilder);
		this.initPanel(PANEL_COMMAND);
	}

	private CommandBuilder builder;

	public final CommandBuilder getBuilder() {
		return builder;
	}

	public final void setBuilder(CommandBuilder builder) {
		this.builder = builder;
	}

	private String historyFolder;

	public final String getHistoryFolder() {
		if (this.historyFolder == null) {
			this.historyFolder = Environment.getHistoryFolder() + File.separator + "commands";
		}
		return historyFolder;
	}

	public final void setHistoryFolder(String historyFolder) {
		this.historyFolder = historyFolder;
	}

	private int last_column_ipadx = BUTTON_WIDTH;
	private JButton button_run = null;
	private CommandTab that = this;
	private Command command = null;

	@Override
	protected void init(JPanel panel) {
		// 执行其他层
		super.init(panel);
		// 仅执行自己相关
		if (!panel.getName().equals(PANEL_COMMAND)) {
			return;
		}
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		int count = 0;
		gridBagConstraints.gridy = count;// 组件的纵坐标
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		gridBagConstraints.insets = new Insets(1, 1, 0, 0);
		// 名称
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.ipadx = 0;
		JLabel label = new JLabel(
				String.format("%s - %s", this.getBuilder().getName(), this.getBuilder().getDescription()));
		label.setFont(new java.awt.Font("Dialog", 3, 15));
		label.setForeground(Color.BLUE);
		label.setToolTipText("double click to edit.");
		label.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// 双击，重载ui
					JFrame frame = (JFrame) that.getRootPane().getParent();
					CommandEditor editor = new CommandEditor(that.getBuilder());
					JDialog dialog = new JDialog(frame, "Command Editor", true);
					dialog.setLayout(new GridBagLayout());
					GridBagConstraints gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.fill = GridBagConstraints.BOTH;
					dialog.setSize(600, 360);
					dialog.add(editor, gridBagConstraints);
					dialog.setLocationRelativeTo(frame);// 移到中间
					dialog.setVisible(true);
					// dialog.pack();
				}
			}
		});
		panel.add(label, gridBagConstraints);
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.ipadx = last_column_ipadx;
		this.button_run = new JButton(MSG_RUN);
		this.button_run.setEnabled(true);
		this.button_run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				that.button_run.setEnabled(false);
				that.onButtonRunClick(that.button_run);
			}
		});
		panel.add(this.button_run, gridBagConstraints);
		gridBagConstraints.gridy++;
		gridBagConstraints.gridheight = 1;
		// 内容
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.ipadx = 0;
		panel.add(new JLabel("History"), gridBagConstraints);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.ipadx = 0;
		JComboBox<?> comboBox = new JComboBox<ComboxItem>(this.getCommandsHistory());
		panel.add(comboBox, gridBagConstraints);
		gridBagConstraints.gridy++;
		// 内容项目
		for (CommandItem commandItem : this.getBuilder().getItems()) {
			this.addCommandItemLine(commandItem, panel, gridBagConstraints);
		}
	}

	private void addCommandItemLine(CommandItem commandItem, JPanel panel, GridBagConstraints gridBagConstraints) {
		// 添加命令内容
		JTextField textField = new JTextField();
		textField.setText(commandItem.getContent());
		textField.setEditable(false);
		textField.setToolTipText(commandItem.getDescription());
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.ipadx = 0;
		panel.add(textField, gridBagConstraints);
		// 添加命令值
		int anchor = gridBagConstraints.anchor;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		commandItem.getValidValues().get();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.ipadx = 0;
		if (commandItem.getValidValues().size() > 0) {
			// 设置了有效值相关
			ComboxItem[] values = new ComboxItem[commandItem.getValidValues().size()];
			for (int i = 0; i < values.length; i++) {
				values[i] = new ComboxItem(commandItem.getValidValues().get(i).getValue(),
						commandItem.getValidValues().get(i).getDescription());
			}
			JComboBox<?> comboBox = new JComboBox<ComboxItem>(values);
			comboBox.setName(String.format("cmb_value_%s", commandItem.hashCode()));
			comboBox.setEditable(false);
			comboBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						int hashCode = Integer.valueOf(comboBox.getName().replace("cmb_value_", ""));
						CommandItem commandItem = that.getBuilder().getItems()
								.firstOrDefault(c -> hashCode == c.hashCode(), true);
						if (commandItem != null) {
							ComboxItem value = (ComboxItem) comboBox.getSelectedItem();
							commandItem.setValue(value.value);
						}
					}
				}
			});
			if (comboBox.getItemCount() > 0) {
				comboBox.setSelectedIndex(0);
			}
			panel.add(comboBox, gridBagConstraints);
		} else {
			JTextField textValue = new JTextField(commandItem.getValue());
			textValue.setName(String.format("txt_value_%s", commandItem.hashCode()));
			textValue.setEditable(commandItem.isEditable());
			if (commandItem.getItems().size() > 0) {
				// 存在子命令
				textValue.setEditable(false);
			}
			textValue.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					int hashCode = Integer.valueOf(textValue.getName().replace("txt_value_", ""));
					CommandItem commandItem = that.getBuilder().getItems().firstOrDefault(c -> hashCode == c.hashCode(),
							true);
					if (commandItem != null) {
						commandItem.setValue(textValue.getText());
					}
				}
			});
			textValue.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

				}
			});
			panel.add(textValue, gridBagConstraints);
		}
		// 添加选择
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridwidth = 1;
		// gridBagConstraints.ipadx = 10;
		JCheckBox checkBox = new JCheckBox();
		checkBox.setName(String.format("chk_selected_%s", commandItem.hashCode()));
		checkBox.setToolTipText("selected and run it");
		checkBox.setSelected(true);
		if (!commandItem.isOptional()) {
			checkBox.setEnabled(false);
		}
		checkBox.addItemListener(new ItemListener() {
			// 监听ui改变，给对象赋值
			@Override
			public void itemStateChanged(ItemEvent e) {
				int hashCode = Integer.valueOf(checkBox.getName().replace("chk_selected_", ""));
				CommandItem commandItem = that.getBuilder().getItems().firstOrDefault(c -> hashCode == c.hashCode(),
						true);
				if (commandItem != null) {
					commandItem.setSelected(e.getStateChange() == ItemEvent.SELECTED ? true : false);
				}
			}
		});
		panel.add(checkBox, gridBagConstraints);
		gridBagConstraints.anchor = anchor;
		// 添加额外内容
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.ipadx = last_column_ipadx;
		JButton button = new JButton("...");
		button.setName(String.format("btn_value_%s", commandItem.hashCode()));
		if (commandItem.getValidValues().getClassName() != null
				&& commandItem.getValidValues().getClassName().equals(JFileChooser.class.getName())) {
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser jfc = new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					jfc.showOpenDialog(null);
					if (button != null && jfc.getSelectedFile() != null) {
						for (Component component : that.getComponents()) {
							if (component.getName() == null) {
								continue;
							}
							if (!component.getName().equals(button.getName().replace("btn_value_", "txt_value_"))) {
								continue;
							}
							if (!(component instanceof JTextField)) {
								continue;
							}
							JTextField textField = (JTextField) component;
							textField.setText(jfc.getSelectedFile().getPath());
						}
					}
				}
			});
		} else {
			button.setEnabled(false);
		}
		panel.add(button, gridBagConstraints);
		gridBagConstraints.gridy++;
		// 添加子项
		for (CommandItem item : commandItem.getItems()) {
			this.addCommandItemLine(item, panel, gridBagConstraints);
		}
	}

	private ComboxItem[] getCommandsHistory() {
		ArrayList<ComboxItem> values = new ArrayList<>();
		values.add(new ComboxItem("-", "-"));
		File folder = new File(this.getHistoryFolder());
		if (folder.exists()) {
			for (File file : folder.listFiles()) {
				if (!file.isFile()) {
					continue;
				}
				if (file.length() == 0) {
					continue;
				}
				if (!file.getName().endsWith(".xml")) {
					continue;
				}
				if (!file.getName().startsWith(this.getBuilder().getName())) {
					continue;
				}
				values.add(new ComboxItem(file.getName(), file.getName()));
			}
		}
		return values.toArray(new ComboxItem[] {});
	}

	protected void onButtonStopClick(JButton button) {
		super.onButtonStopClick(button);
		if (that.command != null) {
			that.command.destroy();
		}
		this.button_run.setEnabled(true);
	}

	protected void onButtonRunClick(JButton button) {
		super.onButtonRunClick(button);
		this.setRunningCommand(that.getBuilder().toCommand());
		this.command = new Command(that.getBuilder());
		this.command.addListener(new CommandListener() {
			@Override
			public void messaged(CommandMessageEvent messageEvent) {
				that.logMessages(messageEvent.getMessage());
			}
		});
		// 另外一个线程运行命令，防止ui阻塞
		new Thread(new Runnable() {
			@Override
			public void run() {
				int ret = command.run();
				// 命令执行完成，自动点击stop钮。
				that.command = null;// 清除命令
				if (ret != 1) {
					// 非用户中断
					that.onButtonStopClick(null);
					// 记录运行命令
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(that.getHistoryFolder());
					stringBuilder.append(File.separator);
					stringBuilder.append(that.getBuilder().getName());
					stringBuilder.append("_");
					stringBuilder.append(
							(new SimpleDateFormat("yyyyMMdd_HHmmssSSS")).format(new Date(System.currentTimeMillis())));
					stringBuilder.append(".xml");
					FileWriter fileWriter = null;
					try {
						File file = new File(stringBuilder.toString());
						if (!file.exists()) {
							file.getParentFile().mkdirs();
							file.createNewFile();
						}
						fileWriter = new FileWriter(file);
						Serializer.toXmlString(that.getBuilder(), true, fileWriter);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (fileWriter != null) {
							try {
								fileWriter.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

				}
			}
		}).start();
	}

	private class ComboxItem {
		public ComboxItem() {
		}

		public ComboxItem(String value, String description) {
			this();
			this.value = value;
			this.description = description;
		}

		public String value;
		public String description;

		@Override
		public String toString() {
			return this.description;
		}
	}
}
