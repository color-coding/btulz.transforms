package org.colorcoding.tools.btulz.util;

public class ExpressionBoolean extends Expression<Boolean> {

	@Override
	public void setLeftValue(Object leftValue) {
		super.setLeftValue(leftValue == null ? false : Boolean.valueOf(String.valueOf(leftValue)));
	}

	@Override
	public void setRightValue(Object rightValue) {
		super.setRightValue(rightValue == null ? false : Boolean.valueOf(String.valueOf(rightValue)));
	}

	@Override
	public boolean result() throws UnsupportedOperationException {
		// 等
		if (this.getOperation() == ExpressionOperation.EQUAL) {
			if (this.getLeftValue() == null || this.getRightValue() == null) {
				return false;
			}
			if (this.getLeftValue() == this.getRightValue()) {
				return true;
			}
			return false;
		}
		// 不等
		else if (this.getOperation() == ExpressionOperation.NOT_EQUAL) {
			if (this.getLeftValue() == null || this.getRightValue() == null) {
				return false;
			}
			if (this.getLeftValue() != this.getRightValue()) {
				return true;
			}
			return false;
		}
		// 且
		else if (this.getOperation() == ExpressionOperation.AND) {
			if (this.getLeftValue() == null || this.getRightValue() == null) {
				return false;
			}
			if (this.getLeftValue() && this.getRightValue()) {
				return true;
			}
			return false;
		}
		// 或
		else if (this.getOperation() == ExpressionOperation.OR) {
			if (this.getLeftValue() == null || this.getRightValue() == null) {
				return false;
			}
			if (this.getLeftValue() || this.getRightValue()) {
				return true;
			}
			return false;
		}
		// 不支持的
		return super.result();
	}

}
