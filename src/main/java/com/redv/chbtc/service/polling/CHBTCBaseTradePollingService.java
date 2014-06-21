package com.redv.chbtc.service.polling;

import org.apache.commons.lang3.math.NumberUtils;

import com.redv.chbtc.CHBTCClient;
import com.redv.chbtc.CHBTCExchange;
import com.xeiam.xchange.ExchangeSpecification;

public class CHBTCBaseTradePollingService extends CHBTCBasePollingService {

	protected final CHBTCClient client;

	/**
	 * @param exchangeSpecification
	 */
	protected CHBTCBaseTradePollingService(
			ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);

		final String tradeApiUrl = exchangeSpecification.getSslUri();
		final String accessKey = exchangeSpecification.getApiKey();
		final String secretKey = exchangeSpecification.getSecretKey();

		final int socketTimeout = NumberUtils
				.toInt((String) exchangeSpecification
						.getParameter(CHBTCExchange.SOCKET_TIMEOUT_PARAMETER));
		final int connectTimeout = NumberUtils
				.toInt((String) exchangeSpecification
						.getParameter(CHBTCExchange.CONNECT_TIMEOUT_PARAMETER));
		final int connectionRequestTimeout = NumberUtils
				.toInt((String) exchangeSpecification
						.getParameter(CHBTCExchange.CONNECTION_REQUEST_TIMEOUT_PARAMETER));

		client = new CHBTCClient(
				tradeApiUrl,
				accessKey,
				secretKey,
				socketTimeout,
				connectTimeout,
				connectionRequestTimeout);
	}

}
