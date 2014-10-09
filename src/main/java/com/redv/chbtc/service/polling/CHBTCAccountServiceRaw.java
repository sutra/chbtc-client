package com.redv.chbtc.service.polling;

import java.io.IOException;

import com.redv.chbtc.domain.AccountInfo;
import com.xeiam.xchange.ExchangeSpecification;

public class CHBTCAccountServiceRaw extends CHBTCBaseTradePollingService {

	/**
	 * @param exchangeSpecification the exchange specification.
	 */
	protected CHBTCAccountServiceRaw(ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
	}

	public AccountInfo getCHBTCAccountInfo() throws IOException {
		return client.getAccountInfo();
	}

}
