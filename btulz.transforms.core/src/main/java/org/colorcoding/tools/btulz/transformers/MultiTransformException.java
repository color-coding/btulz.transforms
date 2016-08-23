package org.colorcoding.tools.btulz.transformers;

public class MultiTransformException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MultiTransformException() {
		super();
	}

	public MultiTransformException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public MultiTransformException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public MultiTransformException(String arg0) {
		super(arg0);
	}

	public MultiTransformException(Throwable arg0) {
		super(arg0);
	}

	public MultiTransformException(Exception[] errors) {
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
