package org.colorcoding.tools.btulz.shell.gui.command;

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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.colorcoding.tools.btulz.shell.command.Command;

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

	private WorkingTabListener listener;

	public final void addWorkingTabListener(WorkingTabListener listener) {
		this.listener = listener;
	}

	public final void removeWorkingTabListener(WorkingTabListener listener) {
		this.listener = null;
	}

	public void changeLayout(String name) {
		JPanel panel = this.getPanel(name);
		if (panel != null) {
			((CardLayout) this.getLayout()).show(this, name);
			if (this.listener != null) {
				this.listener.panelChanged(panel);
			}
		}
	}

	private JTextArea textMessages;
	private JTextField textCommands;
	private JButton button_stop = null;
	private JButton button_run = null;

	protected void init(JPanel panel) {
		if (!panel.getName().equals(PANEL_WORKING)) {
			return;
		}
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		// 名称
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.weightx = 90.0;
		panel.add(new JLabel("Running Command"), gridBagConstraints);
		// 运行的命令
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.weightx = 90.0;
		this.textCommands = new JTextField();
		this.textCommands.setEditable(false);
		panel.add(this.textCommands, gridBagConstraints);
		// 停止钮
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.weightx = 10.0;
		this.button_stop = new JButton(MSG_GO_BACK);
		this.button_stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WorkingTab.this.button_stop.setEnabled(false);
				WorkingTab.this.onButtonStopClick(WorkingTab.this.button_stop);
				WorkingTab.this.button_stop.setEnabled(true);
			}
		});
		panel.add(this.button_stop, gridBagConstraints);
		// 运行钮
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.weightx = 10.0;
		this.button_run = new JButton(MSG_RUN);
		this.button_run.setEnabled(true);
		this.button_run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WorkingTab.this.onButtonRunClick(WorkingTab.this.button_run);
				WorkingTab.this.button_stop.setEnabled(true);
			}
		});
		panel.add(this.button_run, gridBagConstraints);
		// 运行的状态
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		this.textMessages = new JTextArea();
		this.textMessages.setEditable(false);
		panel.add(new JScrollPane(this.textMessages), gridBagConstraints);
	}

	public String getRunningCommand() {
		return this.textCommands.getText();
	}

	public void setRunningCommand(String[] commands) {
		this.textCommands.setText(Command.toCommand(commands));
	}

	protected void onButtonStopClick(JButton button) {
		if (!(button == this.button_stop || button == null)) {
			return;
		}
		this.button_run.setEnabled(true);
		if (this.button_stop.getText().equals(MSG_GO_BACK)) {
			// 返回界面
			this.textCommands.setText(null);
			this.textMessages.setText(null);
			if (!this.getPanels().isEmpty()) {
				this.changeLayout(this.getPanels().get(this.getPanels().size() - 1).getName());
			}
		} else {
			this.button_stop.setText(MSG_GO_BACK);
		}
	}

	protected void onButtonRunClick(JButton button) {
		this.button_run.setEnabled(false);
		this.textMessages.setText("");
		this.button_stop.setText(MSG_STOP);
		this.changeLayout(PANEL_WORKING);
	}

	protected void logMessages(String msg) {
		Document document = this.textMessages.getDocument();
		if (document == null) {
			return;
		}
		try {
			document.insertString(document.getLength(), msg, null);
			document.insertString(document.getLength(), "\n", null);
			this.textMessages.setCaretPosition(document.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

}
