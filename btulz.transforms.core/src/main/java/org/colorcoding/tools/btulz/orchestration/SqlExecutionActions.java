package org.colorcoding.tools.btulz.orchestration;

import java.util.ArrayList;

public class SqlExecutionActions extends ArrayList<ISqlExecutionAction> implements ISqlExecutionActions {

	private static final long serialVersionUID = -3927010628642860016L;

	@Override
	public ISqlExecutionAction create() {
		ISqlExecutionAction item = new SqlExecutionAction();
		if (this.add(item)) {
			return item;
		}
		return null;
	}

}
