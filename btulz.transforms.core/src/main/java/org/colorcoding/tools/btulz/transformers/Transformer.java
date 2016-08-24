package org.colorcoding.tools.btulz.transformers;

import java.util.ArrayList;

public abstract class Transformer implements ITransformer {

	public Transformer() {
		this.initialize();
	}

	/**
	 * 初始化函数，可重载
	 */
	protected void initialize() {

	}

	private boolean keeepResults;

	public final boolean isKeepResults() {
		return this.keeepResults;
	}

	public final void setKeepResults(boolean value) {
		this.keeepResults = value;
	}

	private boolean interruptOnError;

	public final boolean isInterruptOnError() {
		return this.interruptOnError;
	}

	public final void setInterruptOnError(boolean value) {
		this.interruptOnError = value;
	}

	protected void clearResults() {
		this.errors = null;
	}

	private ArrayList<Exception> errors;

	/**
	 * 获取运行时错误
	 * 
	 * @return
	 */
	public final Exception[] getErrors() {
		if (this.errors == null) {
			this.errors = new ArrayList<Exception>();
		}
		return errors.toArray(new Exception[] {});
	}

	/**
	 * 记录错误
	 * 
	 * @param error
	 */
	protected final void logError(Exception error) {
		if (error == null) {
			return;
		}
		if (this.isInterruptOnError()) {
			throw new RuntimeException(error);
		}
		if (this.errors == null) {
			this.errors = new ArrayList<Exception>();
		}
		this.errors.add(error);
	}

}
