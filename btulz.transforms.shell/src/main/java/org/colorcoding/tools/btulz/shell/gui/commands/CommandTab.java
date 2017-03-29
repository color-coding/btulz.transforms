package org.colorcoding.tools.btulz.shell.gui.commands;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
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
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
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
 * 命令执行（UI右侧）
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
	/**
	 * 最多历史信息
	 */
	public static final int MAX_HISTORY_LIST = 10;

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
		gridBagConstraints.gridy = 0;// 组件的纵坐标
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		// 名称
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.weightx = 80.0;
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
					dialog.add(editor);
					dialog.setSize(600, 360);
					dialog.setLocationRelativeTo(frame);// 移到中间
					dialog.setVisible(true);
				}
			}
		});
		panel.add(label, gridBagConstraints);
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.weightx = 20.0;
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
		gridBagConstraints.weightx = 20.0;
		panel.add(new JLabel("History"), gridBagConstraints);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.weightx = 80.0;
		JComboBox<?> comboBox = new JComboBox<ComboxItem>(this.getCommandsHistory());
		comboBox.addItemListener(new ItemListener() {
			private Component getComponent(String name, Container container) {
				Component value = null;
				for (Component component : container.getComponents()) {
					if (component.getName() != null && component.getName().endsWith(name)) {
						value = component;
					} else if (component instanceof Container) {
						value = this.getComponent(name, (Container) component);
					} else if (component instanceof JScrollPane) {
						value = this.getComponent(name, ((JScrollPane) component).getViewport());
					} else {
						continue;
					}
					if (value != null) {
						break;
					}
				}
				return value;
			}

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// 选择新的项
					try {
						ComboxItem comboxItem = (ComboxItem) comboBox.getSelectedItem();
						File file = new File(that.getHistoryFolder() + File.separator + comboxItem.value);
						if (file.isFile() && file.exists()) {
							Object object = Serializer.fromXmlString(new FileInputStream(file), CommandBuilder.class);
							if (object instanceof CommandBuilder) {
								CommandBuilder commandBuilder = (CommandBuilder) object;
								for (CommandItem item : commandBuilder.getItems().getItems()) {
									CommandItem commandItem = that.getBuilder().getItems().firstOrDefault(
											c -> c.getContent() != null && c.getContent().equals(item.getContent()),
											true);
									if (commandItem != null) {
										// 找到了对应项目
										Component component = this.getComponent(
												String.format("_value_%s", commandItem.hashCode()),
												that.getPanel(PANEL_COMMAND));
										if (component instanceof JTextField) {
											JTextField textField = (JTextField) component;
											textField.setText(item.getValue());
										} else if (component instanceof JComboBox<?>) {
											JComboBox<?> comboBox = (JComboBox<?>) component;
											for (int i = 0; i < comboBox.getItemCount(); i++) {
												Object tmp = comboBox.getItemAt(i);
												if (tmp instanceof ComboxItem) {
													ComboxItem cItem = (ComboxItem) tmp;
													if (cItem.value.equals(item.getValue())) {
														comboBox.setSelectedItem(cItem);
														break;
													}
												}
											}
										}
									}
								}
							}
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		panel.add(comboBox, gridBagConstraints);
		gridBagConstraints.gridy++;
		// 内容项目
		GridBagConstraints gridBagConstraintsItem = new GridBagConstraints();
		gridBagConstraintsItem.gridy = 0;// 组件的纵坐标
		gridBagConstraintsItem.fill = GridBagConstraints.BOTH;
		gridBagConstraintsItem.anchor = GridBagConstraints.WEST;
		gridBagConstraintsItem.insets = new Insets(2, 2, 2, 2);
		JPanel panelItem = new JPanel();
		panelItem.setLayout(new GridBagLayout());
		for (CommandItem commandItem : this.getBuilder().getItems()) {
			this.addCommandItemLine(commandItem, panelItem, gridBagConstraintsItem);
		}
		JScrollPane scrollPane = new JScrollPane(panelItem, ScrollPaneLayout.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneLayout.HORIZONTAL_SCROLLBAR_NEVER);

		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		panel.add(scrollPane, gridBagConstraints);
	}

	private static final String control_name_button = "btn_value_";
	private static final String control_name_combox = "cmb_value_";
	private static final String control_name_text = "txt_value_";
	private static final String control_name_checkbox = "chk_value_";

	private void addCommandItemLine(CommandItem commandItem, JPanel panel, GridBagConstraints gridBagConstraints) {
		// 添加命令内容
		JTextField textField = new JTextField();
		textField.setText(commandItem.getContent());
		textField.setEditable(false);
		textField.setToolTipText(commandItem.getDescription());
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 30.0;
		panel.add(textField, gridBagConstraints);
		// 添加命令值
		commandItem.getValidValues().get();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 40.0;
		if (commandItem.getValidValues().size() > 0) {
			// 设置了有效值相关
			ComboxItem[] values = new ComboxItem[commandItem.getValidValues().size() + 1];
			values[0] = ComboxItem.PLEASE_SELECT;
			for (int i = 0; i < values.length - 1; i++) {
				values[i + 1] = new ComboxItem(commandItem.getValidValues().get(i).getValue(),
						commandItem.getValidValues().get(i).getDescription());
			}
			JComboBox<?> comboBox = new JComboBox<ComboxItem>(values);
			comboBox.setName(control_name_combox + commandItem.hashCode());
			comboBox.setEditable(false);
			comboBox.addItemListener(new ItemListener() {
				private CommandItem commandItem = null;

				public CommandItem getCommandItem() {
					if (commandItem == null) {
						int hashCode = Integer.valueOf(comboBox.getName().replace(control_name_combox, ""));
						this.commandItem = that.getBuilder().getItems().firstOrDefault(c -> hashCode == c.hashCode(),
								true);
					}
					return this.commandItem;
				}

				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						if (this.getCommandItem() != null) {
							ComboxItem value = (ComboxItem) comboBox.getSelectedItem();
							this.getCommandItem().setValue(value.value);
						}
					}
				}
			});
			panel.add(comboBox, gridBagConstraints);
		} else {
			JTextField textValue = new JTextField(commandItem.getValue());
			textValue.setName(control_name_text + commandItem.hashCode());
			textValue.setEditable(commandItem.isEditable());
			if (commandItem.getItems().size() > 0) {
				// 存在子命令
				textValue.setEditable(false);
			}
			textValue.getDocument().addDocumentListener(new DocumentListener() {
				private CommandItem commandItem = null;

				public CommandItem getCommandItem() {
					if (commandItem == null) {
						int hashCode = Integer.valueOf(textValue.getName().replace(control_name_text, ""));
						this.commandItem = that.getBuilder().getItems().firstOrDefault(c -> hashCode == c.hashCode(),
								true);
					}
					return this.commandItem;
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					if (this.getCommandItem() != null) {
						this.getCommandItem().setValue(textValue.getText());
					}
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					if (this.getCommandItem() != null) {
						this.getCommandItem().setValue(textValue.getText());
					}
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					if (this.getCommandItem() != null) {
						this.getCommandItem().setValue(textValue.getText());
					}
				}
			});
			panel.add(textValue, gridBagConstraints);
		}
		// 添加选择
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 10.0;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		JCheckBox checkBox = new JCheckBox();
		checkBox.setName(control_name_checkbox + commandItem.hashCode());
		checkBox.setToolTipText("selected and run it");
		checkBox.setSelected(true);
		if (!commandItem.isOptional()) {
			checkBox.setEnabled(false);
		}
		checkBox.addItemListener(new ItemListener() {

			private CommandItem commandItem = null;

			public CommandItem getCommandItem() {
				if (commandItem == null) {
					int hashCode = Integer.valueOf(checkBox.getName().replace(control_name_checkbox, ""));
					this.commandItem = that.getBuilder().getItems().firstOrDefault(c -> hashCode == c.hashCode(), true);
				}
				return this.commandItem;
			}

			// 监听ui改变，给对象赋值
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (this.getCommandItem() != null) {
					this.getCommandItem().setSelected(e.getStateChange() == ItemEvent.SELECTED ? true : false);
				}
			}
		});
		panel.add(checkBox, gridBagConstraints);
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		// 添加额外内容
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 20.0;
		JButton button = new JButton("...");
		button.setName(control_name_button + commandItem.hashCode());
		if (commandItem.getValidValues().getClassName() != null
				&& commandItem.getValidValues().getClassName().equals(JFileChooser.class.getName())) {
			button.addActionListener(new ActionListener() {
				private JTextField textField;

				private JTextField getTextField(Container container) {
					JTextField value = null;
					for (Component component : container.getComponents()) {
						if (component instanceof JTextField) {
							if (component.getName() != null && component.getName()
									.equals(button.getName().replace(control_name_button, control_name_text))) {
								value = (JTextField) component;
							}
						} else if (component instanceof Container) {
							value = this.getTextField((Container) component);
						} else if (component instanceof JScrollPane) {
							value = this.getTextField(((JScrollPane) component).getViewport());
						} else {
							continue;
						}
						if (value != null) {
							break;
						}
					}
					return value;
				}

				public JTextField geTextField() {
					if (textField == null) {
						this.textField = this.getTextField(that.getPanel(PANEL_COMMAND));
					}
					return this.textField;
				}

				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser jfc = new JFileChooser();
					if (commandItem.getValidValues().getDefinitions() != null
							&& !commandItem.getValidValues().getDefinitions().isEmpty()) {
						// 存在额外定义
						for (String item : commandItem.getValidValues().getDefinitions().split(";")) {
							if (item == null || item.isEmpty()) {
								continue;
							}
							if (item.indexOf("=") > 0) {
								// MultiSelectionEnabled=true
								String name = item.split("=")[0];
								String value = item.split("=")[1];
								for (Method method : JFileChooser.class.getDeclaredMethods()) {
									if (method.getParameterTypes().length != 1) {
										continue;
									}
									if (method.getName().endsWith(name)) {
										Class<?> valueType = method.getParameterTypes()[0];
										Object nValue = null;
										if (valueType.equals(boolean.class)) {
											nValue = Boolean.valueOf(value);
										}
										if (nValue != null) {
											try {
												method.invoke(jfc, nValue);
											} catch (IllegalAccessException | IllegalArgumentException
													| InvocationTargetException e1) {
												that.logMessages(e1.toString());
											}
										}
									}
								}
							}
						}
					}
					// 设置打开路径
					if (this.geTextField() != null) {
						File file = new File(this.geTextField().getText());
						if (file.exists()) {
							if (file.isFile()) {
								file = file.getParentFile();
							}
							jfc.setCurrentDirectory(file);
						}
					}
					jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					jfc.showOpenDialog(null);
					if (button != null && jfc.getSelectedFile() != null) {
						if (this.geTextField() != null) {
							if (jfc.isMultiSelectionEnabled()) {
								StringBuilder stringBuilder = new StringBuilder();
								for (File item : jfc.getSelectedFiles()) {
									if (stringBuilder.length() > 0) {
										stringBuilder.append(";");
									}
									stringBuilder.append(item.getPath());
								}
								this.geTextField().setText(stringBuilder.toString());
							} else {
								this.geTextField().setText(jfc.getSelectedFile().getPath());
							}
						}
					}
				}
			});
		} else if (commandItem.getValidValues().getClassName() != null
				&& commandItem.getValidValues().getClassName().equals(java.util.UUID.class.getName())) {
			button.addActionListener(new ActionListener() {
				private JTextField textField;

				private JTextField getTextField(Container container) {
					JTextField value = null;
					for (Component component : container.getComponents()) {
						if (component instanceof JTextField) {
							if (component.getName() != null && component.getName()
									.equals(button.getName().replace(control_name_button, control_name_text))) {
								value = (JTextField) component;
							}
						} else if (component instanceof Container) {
							value = this.getTextField((Container) component);
						} else if (component instanceof JScrollPane) {
							value = this.getTextField(((JScrollPane) component).getViewport());
						} else {
							continue;
						}
						if (value != null) {
							break;
						}
					}
					return value;
				}

				public JTextField geTextField() {
					if (textField == null) {
						this.textField = this.getTextField(that.getPanel(PANEL_COMMAND));
					}
					return this.textField;
				}

				@Override
				public void actionPerformed(ActionEvent e) {
					this.geTextField().setText(UUID.randomUUID().toString());
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
		values.add(ComboxItem.PLEASE_SELECT);
		File folder = new File(this.getHistoryFolder());
		if (folder.exists()) {
			File[] files = folder.listFiles();
			Arrays.sort(files, new Comparator<File>() {
				public int compare(File f1, File f2) {
					long diff = f1.lastModified() - f2.lastModified();
					if (diff > 0)
						return 1;
					else if (diff == 0)
						return 0;
					else
						return -1;
				}

				public boolean equals(Object obj) {
					return true;
				}
			});
			for (File file : files) {
				if (values.size() > MAX_HISTORY_LIST) {
					break;
				}
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
				that.onButtonStopClick(null);
				if (ret != 1) {
					// 非用户中断
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

	private static class ComboxItem {
		public static ComboxItem PLEASE_SELECT = new ComboxItem("", " - please select - ");

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
