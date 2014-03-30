package com.redv.chbtc;

import java.io.IOException;

import com.redv.chbtc.domain.CHBTCError;

public class CHBTCClientException extends IOException {

	private static final long serialVersionUID = 2013113001L;

	private CHBTCError error;

	public CHBTCClientException() {
	}

	public CHBTCClientException(String message) {
		super(message);
	}

	public CHBTCClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public CHBTCClientException(CHBTCError error) {
		super(error.getMessage());
		this.error = error;
	}

	public CHBTCError getError() {
		return error;
	}

}
