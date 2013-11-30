package com.redv.chbtc.domain;

public class TickerResponse extends AbstractObject {

	private static final long serialVersionUID = 2013112501L;

	private Ticker ticker;

	public TickerResponse() {

	}

	public TickerResponse(Ticker ticker) {
		this.ticker = ticker;
	}

	public Ticker getTicker() {
		return ticker;
	}

}
