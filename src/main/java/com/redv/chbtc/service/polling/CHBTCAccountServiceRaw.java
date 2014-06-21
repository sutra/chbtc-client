package com.redv.chbtc.service.polling;

import com.redv.chbtc.CHBTC;
import com.redv.chbtc.domain.AccountInfo;
import com.xeiam.xchange.ExchangeSpecification;

public class CHBTCAccountServiceRaw extends CHBTCBaseTradePollingService {

	/**
	 * @param exchangeSpecification
	 */
	protected CHBTCAccountServiceRaw(ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
	}

	public AccountInfo getCHBTCAccountInfo() {
		return chbtc.getAccountInfo(
				CHBTC.METHOD_GET_ACCOUNT_INFO,
				accessKey,
				sign(CHBTC.METHOD_GET_ACCOUNT_INFO),
				getReqTime());
	}

}
