package org.oxerr.chbtc.service.polling;

import java.io.IOException;

import org.oxerr.chbtc.dto.AccountInfo;

import com.xeiam.xchange.Exchange;

public class CHBTCAccountServiceRaw extends CHBTCBaseTradePollingService {

	protected CHBTCAccountServiceRaw(Exchange exchange) {
		super(exchange);
	}

	public AccountInfo getCHBTCAccountInfo() throws IOException {
		return client.getAccountInfo();
	}

}
