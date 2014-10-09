package com.redv.chbtc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountInfo extends CHBTCError {

	private static final long serialVersionUID = 2014063001L;

	private final Result result;

	public AccountInfo(
			@JsonProperty("code") int code,
			@JsonProperty("message") String message,
			@JsonProperty("result") final Result result) {
		super(code, message);
		this.result = result;
	}

	public Result getResult() {
		return result;
	}

}
