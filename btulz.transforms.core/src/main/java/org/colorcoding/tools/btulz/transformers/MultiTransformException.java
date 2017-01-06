package org.colorcoding.tools.btulz.transformers;

public class MultiTransformException extends Exception {

	private static final long serialVersionUID = 2405395597366247548L;

	public MultiTransformException() {
		super();
	}

	public MultiTransformException(String message) {
		super(message);
	}

	public MultiTransformException(String message, Exception[] errors) {
		this(message);
		this.errors = errors;
	}

	private Exception[] errors;

	public Exception[] getExceptions() {
		if (this.errors == null) {
			this.errors = new Exception[] {};
		}
		return this.errors;
	}
}
