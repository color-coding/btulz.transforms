package org.colorcoding.tools.btulz.util;

public abstract class Expression<T> {

	private T leftValue;

	public final T getLeftValue() {
		return leftValue;
	}

	@SuppressWarnings("unchecked")
	public void setLeftValue(Object leftValue) {
		this.leftValue = (T) leftValue;
	}

	private ExpressionOperation operation;

	public final ExpressionOperation getOperation() {
		return operation;
	}

	public final void setOperation(ExpressionOperation operation) {
		this.operation = operation;
	}

	private T rightValue;

	public final T getRightValue() {
		return rightValue;
	}

	@SuppressWarnings("unchecked")
	public void setRightValue(Object rightValue) {
		this.rightValue = (T) rightValue;
	}

	public boolean result() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return String.format("{Expression: %s %s %s}", this.getLeftValue(), this.getOperation(), this.getRightValue());
	}

}
