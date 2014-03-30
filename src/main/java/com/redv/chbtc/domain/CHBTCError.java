package com.redv.chbtc.domain;

public class CHBTCError extends AbstractObject {

	public static final int SUCCESS = 1000;

	public static final int ERROR_TIPS = 1001;

	public static final int INTERNAL_ERROR = 1002;

	public static final int VALIDATE_NO_PASS = 1003;

	public static final int INSUFFICIENT_CNY_BALANCE = 2001;

	public static final int INSUFFICIENT_BTC_BALANCE = 2002;

	public static final int INSUFFICIENT_LTC_BALANCE = 2003;

	public static final int NOT_FOUND_ORDER = 3001;

	public static final int INVALID_MONEY = 3002;

	public static final int INVALID_AMOUNT = 3003;

	public static final int NO_SUCH_USER = 3004;

	public static final int INVALID_ARGUMENTS = 3005;

	public static final int INVALID_IP_ADDRESS = 3006;

	public static final int API_LOCKED_OR_NOT_ENABLED = 4001;

	public static final int REQUEST_TOO_FREQUENTLY = 4002;

	private static final long serialVersionUID = 2014033001L;

	private int code;

	private String message;

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public boolean isSuccess() {
		return getCode() == SUCCESS || getCode() == 0;
	}

}
