package com.redv.chbtc.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Trade extends AbstractObject {

	private static final long serialVersionUID = 2014063001L;

	/**
	 * Unix time, in seconds.
	 */
	private final String date;

	private final BigDecimal price;

	private final BigDecimal amount;

	private final String tid;

	/**
	 * buy/sell
	 */
	private final String type;

	public Trade(
			@JsonProperty("date") final String date,
			@JsonProperty("price") final BigDecimal price,
			@JsonProperty("amount") final BigDecimal amount,
			@JsonProperty("tid") String tid,
			@JsonProperty("type") String type) {
		this.date = date;
		this.price = price;
		this.amount = amount;
		this.tid = tid;
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getTid() {
		return tid;
	}

	public String getType() {
		return type;
	}

}