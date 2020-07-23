package org.colorcoding.tools.btulz.util;

public class ExpressionString extends Expression<String> {

	@Override
	public void setLeftValue(Object leftValue) {
		super.setLeftValue(leftValue == null ? leftValue : String.valueOf(leftValue));
	}

	@Override
	public void setRightValue(Object rightValue) {
		super.setRightValue(rightValue == null ? rightValue : String.valueOf(rightValue));
	}

	@Override
	public boolean result() throws UnsupportedOperationException {
		// 开始与
		if (this.getOperation() == ExpressionOperation.BEGIN_WITH) {
			if (this.getLeftValue() == null || this.getRightValue() == null) {
				return false;
			}
			if (this.getLeftValue().startsWith(this.getRightValue())) {
				return true;
			}
			return false;
		}
		// 结束于
		else if (this.getOperation() == ExpressionOperation.END_WITH) {
			if (this.getLeftValue() == null || this.getRightValue() == null) {
				return false;
			}
			if (this.getLeftValue().endsWith(this.getRightValue())) {
				return true;
			}
			return false;
		}
		// 非开始于
		else if (this.getOperation() == ExpressionOperation.NOT_BEGIN_WITH) {
			if (this.getLeftValue() == null || this.getRightValue() == null) {
				return false;
			}
			if (!this.getLeftValue().startsWith(this.getRightValue())) {
				return true;
			}
			return false;
		}
		// 非结束于
		else if (this.getOperation() == ExpressionOperation.NOT_END_WITH) {
			if (this.getLeftValue() == null || this.getRightValue() == null) {
				return false;
			}
			if (!this.getLeftValue().endsWith(this.getRightValue())) {
				return true;
			}
			return false;
		}
		// 包含
		else if (this.getOperation() == ExpressionOperation.CONTAIN) {
			if (this.getLeftValue() == null || this.getRightValue() == null) {
				return false;
			}
			if (this.getLeftValue().indexOf(this.getRightValue()) >= 0) {
				return true;
			}
			return false;
		}
		// 不包含
		else if (this.getOperation() == ExpressionOperation.NOT_CONTAIN) {
			if (this.getLeftValue() == null || this.getRightValue() == null) {
				return false;
			}
			if (this.getLeftValue().indexOf(this.getRightValue()) < 0) {
				return true;
			}
			return false;
		}
		// 等于
		else if (this.getOperation() == ExpressionOperation.EQUAL) {
			if (this.getLeftValue() == this.getRightValue()) {
				return true;
			}
			if (this.getLeftValue() == null || this.getRightValue() == null) {
				return false;
			}
			if (this.getLeftValue().equalsIgnoreCase(this.getRightValue())) {
				return true;
			}
			return false;
		}
		// 不等于
		else if (this.getOperation() == ExpressionOperation.NOT_EQUAL) {
			if (this.getLeftValue() == this.getRightValue()) {
				return false;
			}
			if (this.getLeftValue() == null || this.getRightValue() == null) {
				return true;
			}
			if (this.getLeftValue().equalsIgnoreCase(this.getRightValue())) {
				return false;
			}
			return true;
		}
		// 其他
		return super.result();
	}

}
