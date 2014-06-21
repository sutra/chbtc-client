package com.redv.chbtc.service.polling;

import org.apache.commons.lang3.math.NumberUtils;

import com.redv.chbtc.CHBTCClient;
import com.xeiam.xchange.ExchangeSpecification;

public class CHBTCBaseTradePollingService extends CHBTCBasePollingService {

	protected final CHBTCClient client;

	/**
	 * @param exchangeSpecification
	 */
	protected CHBTCBaseTradePollingService(
			ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
		final String baseUrl = exchangeSpecification.getSslUri();
		final String accessKey = exchangeSpecification.getApiKey();
		final String secretKey = exchangeSpecification.getSecretKey();
		final int socketTimeout = NumberUtils.toInt((String) exchangeSpecification.getParameter("socketTimeout"));
		final int connectTimeout = NumberUtils.toInt((String) exchangeSpecification.getParameter("connectTimeout"));
		final int connectionRequestTimeout = NumberUtils.toInt((String) exchangeSpecification.getParameter("connectionRequestTimeout"));

		client = new CHBTCClient(
				baseUrl,
				accessKey,
				secretKey,
				socketTimeout,
				connectTimeout,
				connectionRequestTimeout);

	}

}
