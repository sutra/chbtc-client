package org.oxerr.chbtc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TickerResponse extends BaseObject {

	private static final long serialVersionUID = 2014063001L;

	private final Ticker ticker;

	public TickerResponse(@JsonProperty("ticker") final Ticker ticker) {
		this.ticker = ticker;
	}

	public Ticker getTicker() {
		return ticker;
	}

}
