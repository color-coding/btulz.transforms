package org.colorcoding.tools.btulz.orchestration;

import java.util.ArrayList;

public class SqlExecutionActionSteps extends ArrayList<ISqlExecutionActionStep> implements ISqlExecutionActionSteps {

	private static final long serialVersionUID = 8959103759752136142L;

	@Override
	public ISqlExecutionActionStep create() {
		ISqlExecutionActionStep item = new SqlExecutionActionStep();
		if (this.add(item)) {
			return item;
		}
		return null;
	}

}
