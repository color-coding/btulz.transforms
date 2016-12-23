package org.colorcoding.tools.btulz.shell.gui.commands;

import javax.swing.JPanel;

/**
 * 工作签事件
 * 
 * @author Niuren.Zhu
 *
 */
public interface WorkingTabListener {

	/**
	 * 页签切换
	 * 
	 * @param panel
	 */
	void panelChanged(JPanel panel);
}
