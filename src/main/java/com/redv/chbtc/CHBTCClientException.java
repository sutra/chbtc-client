package com.redv.chbtc;

import java.io.IOException;

public class CHBTCClientException extends IOException {

	private static final long serialVersionUID = 2013113001L;

	public CHBTCClientException() {
	}

	public CHBTCClientException(String message) {
		super(message);
	}

	public CHBTCClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
