package com.redv.chbtc.service.polling;

import com.redv.chbtc.domain.AccountInfo;
import com.xeiam.xchange.ExchangeSpecification;

public class CHBTCAccountServiceRaw extends CHBTCBaseTradePollingService {

	private static final String METHOD_GET_ACCOUNT_INFO = "getAccountInfo";

	/**
	 * @param exchangeSpecification
	 */
	protected CHBTCAccountServiceRaw(ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
	}

	public AccountInfo getCHBTCAccountInfo() {
		return chbtc.getAccountInfo(
				METHOD_GET_ACCOUNT_INFO,
				accessKey,
				sign(METHOD_GET_ACCOUNT_INFO),
				getReqTime());
	}

}
