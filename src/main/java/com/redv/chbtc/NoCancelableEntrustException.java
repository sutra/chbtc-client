package com.redv.chbtc;

public class NoCancelableEntrustException extends CHBTCClientException {

	private static final long serialVersionUID = 2013121401L;

	public NoCancelableEntrustException() {
	}

	public NoCancelableEntrustException(String message) {
		super(message);
	}

}
