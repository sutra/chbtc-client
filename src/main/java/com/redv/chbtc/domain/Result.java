package com.redv.chbtc.domain;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result extends AbstractObject {

	private static final long serialVersionUID = 2014063001L;

	private final Base base;

	/**
	 * 可用资金、货币。
	 */
	private final Map<String, Balance> balance;

	/**
	 * 冻结资金、货币。
	 */
	private final Map<String, Balance> frozen;

	private final Map<String, BigDecimal> p2p;

	public Result(
			@JsonProperty("base") Base base,
			@JsonProperty("balance") Map<String, Balance> balance,
			@JsonProperty("frozen") Map<String, Balance> frozen,
			@JsonProperty("p2p") Map<String, BigDecimal> p2p) {
		this.base = base;
		this.balance = balance;
		this.frozen = frozen;
		this.p2p = p2p;
	}

	public Base getBase() {
		return base;
	}

	public Map<String, Balance> getBalance() {
		return balance;
	}

	public Map<String, Balance> getFrozen() {
		return frozen;
	}

	public Map<String, BigDecimal> getP2p() {
		return p2p;
	}

}
