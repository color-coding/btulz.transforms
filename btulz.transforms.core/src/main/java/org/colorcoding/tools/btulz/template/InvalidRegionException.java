package org.colorcoding.tools.btulz.template;

/**
 * 无效的区域异常
 * 
 * @author Niuren.Zhu
 *
 */
public class InvalidRegionException extends Exception {

	private static final long serialVersionUID = -580802517051513375L;

	public InvalidRegionException() {
		super();
	}

	public InvalidRegionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidRegionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidRegionException(String message) {
		super(message);
	}

	public InvalidRegionException(Throwable cause) {
		super(cause);
	}

}
