package org.colorcoding.tools.btulz.shell.gui.command;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;

import org.colorcoding.tools.btulz.shell.command.CommandBuilder;
import org.colorcoding.tools.btulz.shell.command.CommandManager;

/**
 * 主界面
 * 
 * @author Niuren.Zhu
 *
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = -8387552310875230969L;

	public MainFrame() {
		super("btulz.transforms.shell");
		URL url = this.getClass().getResource("/images/icon.png");
		this.setIconImage(new ImageIcon(url).getImage());
	}

	public void initialize() {
		// 设置窗体
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		// 添加菜单
		this.setJMenuBar(this.drawMenus());
		// 添加左边区域
		this.add(this.drawLeft(), BorderLayout.WEST);
		// 添加右边区域
		this.add(this.workingPane = this.drawRight(), BorderLayout.CENTER);
	}

	protected JMenuBar drawMenus() {
		JMenuBar menuBar = new JMenuBar();
		// 帮助菜单项
		JMenu menu = new JMenu("Help");
		// 帮助-关于
		JMenuItem itemAbout = new JMenuItem("About");
		itemAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.this.showWorkingTab(new AboutTab());
			}
		});
		menu.add(itemAbout);
		menuBar.add(menu);
		return menuBar;
	}

	protected JPanel drawLeft() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JLabel label = new JLabel("Commands");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setFont(new java.awt.Font("Dialog", 3, 18));
		label.setForeground(Color.BLUE);
		label.setToolTipText("double click to reload.");
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
					MainFrame.this.dispose();
					// 重新加载命令
					CommandManager.create().initialize();
					new MainFrame().display();
				}
			}
		});
		panel.add(label);
		BuilderTab builderPane = new BuilderTab();
		builderPane.setName("pane_builder");
		JScrollPane scrollPane = new JScrollPane(builderPane, ScrollPaneLayout.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneLayout.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.add(scrollPane);
		builderPane.addBuilderTabListener(new BuilderTabListener() {
			@Override
			public void builderSelected(CommandBuilder builder) {
				MainFrame.this.showWorkingTab(new CommandTab(builder));
			}
		});
		return panel;
	}

	protected JTabbedPane drawRight() {
		JTabbedPane panel = new JTabbedPane();
		panel.setName("pane_working");
		return panel;
	}

	public void display() {
		this.initialize();
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screensize.getWidth();
		int height = (int) screensize.getHeight();
		if (height / 9 * 16 < width) {
			width = height / 9 * 16;
		}
		this.setSize(width / 3 * 2, height / 3 * 2);
		this.setLocationRelativeTo(null);// 移到中间
		this.setVisible(true);
	}

	JTabbedPane workingPane;

	@Override
	public void pack() {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screensize.getWidth();
		int height = (int) screensize.getHeight();
		if (height / 9 * 16 < width) {
			width = height / 9 * 16;
		}
		if (this.getWidth() <= width && this.getHeight() < height) {
			super.pack();
		}
	}

	public void showWorkingTab(WorkingTab tab) {
		if (this.workingPane == null) {
			return;
		}
		this.workingPane.addTab(tab.getTitle(), tab);
		this.workingPane.setSelectedComponent(tab);
	}

}
