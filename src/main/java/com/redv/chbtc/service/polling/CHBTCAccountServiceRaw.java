package com.redv.chbtc.service.polling;

import java.io.IOException;

import com.redv.chbtc.domain.AccountInfo;
import com.xeiam.xchange.Exchange;

public class CHBTCAccountServiceRaw extends CHBTCBaseTradePollingService {

	protected CHBTCAccountServiceRaw(Exchange exchange) {
		super(exchange);
	}

	public AccountInfo getCHBTCAccountInfo() throws IOException {
		return client.getAccountInfo();
	}

}
