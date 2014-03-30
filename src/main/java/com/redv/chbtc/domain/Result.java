package com.redv.chbtc.domain;

import java.util.Map;

public class Result extends AbstractObject {

	private static final long serialVersionUID = 2014033001L;

	private Base base;

	/**
	 * 可用资金、货币。
	 */
	private Map<String, Balance> balance;

	/**
	 * 冻结资金、货币。
	 */
	private Map<String, Balance> frozen;

	public Base getBase() {
		return base;
	}

	public Map<String, Balance> getBalance() {
		return balance;
	}

	public Map<String, Balance> getFrozen() {
		return frozen;
	}

}
