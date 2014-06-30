package com.redv.chbtc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderResponse extends CHBTCError {

	private static final long serialVersionUID = 2014063001L;

	private final long id;

	public OrderResponse(
			@JsonProperty("code") final int code,
			@JsonProperty("message") final String message, 
			@JsonProperty("id") final long id) {
		super(code, message);
		this.id = id;
	}

	public long getId() {
		return id;
	}

}
