package org.colorcoding.tools.btulz.shell.gui.commands;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class WorkingTab extends JPanel {

	private static final long serialVersionUID = 7047384462753675622L;
	/**
	 * 工作层
	 */
	public static final String PANEL_WORKING = "working_panel";
	public static final String MSG_GO_BACK = "go back";
	public static final String MSG_STOP = "stop";
	public static final String MSG_RUN = "run";
	/**
	 * 默认按钮长度
	 */
	public static final int BUTTON_WIDTH = 40;

	public WorkingTab() {
		this.setLayout(new CardLayout());
		this.initPanel(PANEL_WORKING);
	}

	public WorkingTab(String title) {
		this();
		this.setTitle(title);
	}

	private String title;

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {
		this.title = title;
	}

	private List<JPanel> panels;

	public final List<JPanel> getPanels() {
		if (this.panels == null) {
			this.panels = new ArrayList<>();
		}
		return panels;
	}

	public JPanel initPanel(String name) {
		if (this.getPanel(name) != null) {
			throw new RuntimeException(String.format("%s already exists.", name));
		}
		JPanel panel = new JPanel();
		panel.setName(name);
		this.init(panel);
		this.add(panel, name);
		this.getPanels().add(panel);
		this.changeLayout(name);
		return panel;
	}

	public JPanel getPanel(String name) {
		for (JPanel panel : this.getPanels()) {
			if (panel.getName().equals(name)) {
				return panel;
			}
		}
		return null;
	}

	public void changeLayout(String name) {
		((CardLayout) this.getLayout()).show(this, name);
	}

	private WorkingTab that = this;
	private JTextArea textArea;
	private JTextField textField;
	private JButton button_stop = null;
	private JButton button_run = null;

	protected void init(JPanel panel) {
		if (!panel.getName().equals(PANEL_WORKING)) {
			return;
		}
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 1, 0, 0);
		// 名称
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.ipadx = 0;
		panel.add(new JLabel("Running Command"), gridBagConstraints);
		// 停止钮
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.ipadx = BUTTON_WIDTH;
		this.button_stop = new JButton(MSG_GO_BACK);
		this.button_stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				that.button_stop.setEnabled(false);
				that.onButtonStopClick(that.button_stop);
				that.button_stop.setEnabled(true);
			}
		});
		panel.add(this.button_stop, gridBagConstraints);
		// 运行的命令
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.ipadx = 0;
		this.textField = new JTextField();
		this.textField.setEditable(false);
		panel.add(this.textField, gridBagConstraints);
		// 运行钮
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.ipadx = BUTTON_WIDTH;
		this.button_run = new JButton(MSG_RUN);
		this.button_run.setEnabled(true);
		this.button_run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				that.onButtonRunClick(that.button_run);
				that.button_stop.setEnabled(true);
			}
		});
		panel.add(this.button_run, gridBagConstraints);
		// 运行的状态
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.ipadx = 0;
		gridBagConstraints.gridwidth = 2;
		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		panel.add(this.textArea, gridBagConstraints);
	}

	public String getRunningCommand() {
		return this.textField.getText();
	}

	public void setRunningCommand(String command) {
		this.textField.setText(command);
	}

	protected void onButtonStopClick(JButton button) {
		if (!(button == this.button_stop || button == null)) {
			return;
		}
		this.button_run.setEnabled(true);
		if (this.button_stop.getText().equals(MSG_GO_BACK)) {
			// 返回界面
			if (!this.getPanels().isEmpty()) {
				this.changeLayout(this.getPanels().get(this.getPanels().size() - 1).getName());
			}
		} else {
			this.button_stop.setText(MSG_GO_BACK);
		}
	}

	protected void onButtonRunClick(JButton button) {
		this.button_run.setEnabled(false);
		this.textArea.setText("");
		this.button_stop.setText(MSG_STOP);
		this.changeLayout(PANEL_WORKING);
	}

	protected void logMessages(String msg) {
		this.textArea.setText(msg);
	}

}
