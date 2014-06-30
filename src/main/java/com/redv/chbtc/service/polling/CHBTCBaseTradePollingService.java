package com.redv.chbtc.service.polling;

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

		final int socketTimeout = (Integer) exchangeSpecification
				.getParameter(CHBTCExchange.SOCKET_TIMEOUT_PARAMETER);
		final int connectTimeout = (Integer) exchangeSpecification
				.getParameter(CHBTCExchange.CONNECT_TIMEOUT_PARAMETER);
		final int connectionRequestTimeout = (Integer) exchangeSpecification
				.getParameter(CHBTCExchange.CONNECTION_REQUEST_TIMEOUT_PARAMETER);

		client = new CHBTCClient(
				tradeApiUrl,
				accessKey,
				secretKey,
				socketTimeout,
				connectTimeout,
				connectionRequestTimeout);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void finalize() throws Throwable {
		this.client.close();
		super.finalize();
	}

}
