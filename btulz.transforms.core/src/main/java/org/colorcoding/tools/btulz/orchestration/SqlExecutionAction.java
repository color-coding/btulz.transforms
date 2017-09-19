package org.colorcoding.tools.btulz.orchestration;

import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "SqlExecutionAction", namespace = Environment.NAMESPACE_BTULZ_ORCHESTRATION)
public class SqlExecutionAction extends ExecutionAction implements ISqlExecutionAction {

	// @XmlElementWrapper(name = "Steps")
	@XmlElement(name = "Step", type = SqlExecutionActionStep.class)
	private ISqlExecutionActionSteps steps = new SqlExecutionActionSteps();

	@Override
	public ISqlExecutionActionSteps getSteps() {
		if (this.steps == null) {
			this.steps = new SqlExecutionActionSteps();
		}
		return this.steps;
	}

	private Statement statement;

	protected Statement getStatement() {
		return statement;
	}

	private void setStatement(Statement value) {
		this.statement = value;
	}

	@Override
	public final void execute(Statement statement) throws Exception {
		this.setStatement(statement);
		this.curStep = 0;
		this.execute();
	}

	private boolean keepStepResult = true;

	@XmlAttribute(name = "KeepStepResult")
	public boolean isKeepStepResult() {
		return keepStepResult;
	}

	public void setKeepStepResult(boolean value) {
		this.keepStepResult = value;
	}

	private boolean isIsolated;

	@XmlAttribute(name = "Isolated")
	public boolean isIsolated() {
		return isIsolated;
	}

	public void setIsolated(boolean value) {
		this.isIsolated = value;
	}

	private String dbUrl;

	@XmlAttribute(name = "DbUrl")
	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	private int curStep = 0;// 当前步骤
	private int lastStep = 0;// 上次运行的步骤
	private Object curValue;// 当前的状态值

	@Override
	public void execute() throws Exception {
		long startTime = System.currentTimeMillis();
		Environment.getLogger().info(String.format("begin action [%s].", this.getName()));
		Statement statement = this.getStatement();
		if (statement == null) {
			throw new SQLException("database statement is not initialized.");
		}
		try {
			for (int i = curStep; i < this.getSteps().size(); i++) {
				ISqlExecutionActionStep step = this.getSteps().get(i);
				step.setStatement(this.getStatement());
				this.curStep = i;
				if (step.check(this.curValue)) {
					Object value = step.execute();
					if (step.isQuery()) {					
						this.curValue = value;
					}
					this.lastStep = i;
					Environment.getLogger()
							.debug(String.format("status value changed to [%s].", this.curValue, step.getName()));
				}
				if (!this.isKeepStepResult() && this.curStep > this.lastStep) {
					this.curValue = null;
					Environment.getLogger().debug(String.format("clear status value.", this.curValue, step.getName()));
				}
			}
		} catch (Exception e) {
			// 检查后续步骤，是否有对此错误的处理
			// TODO:错误处理

			// 没有处理此错误，则抛出
			throw e;
		}
		long endTime = System.currentTimeMillis();
		float excTime = (float) (endTime - startTime) / 1000;
		Environment.getLogger().info(String.format("end action [%s], used %s second.", this.getName(), excTime));
	}

}
