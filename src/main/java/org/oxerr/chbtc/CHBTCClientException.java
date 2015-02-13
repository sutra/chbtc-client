package org.oxerr.chbtc;

import java.io.IOException;

import org.oxerr.chbtc.dto.CHBTCError;

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

	public int getErrorCode() {
		return error.getCode();
	}

}
