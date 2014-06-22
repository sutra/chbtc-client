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

		final Integer socketTimeout = (Integer) exchangeSpecification
				.getParameter(CHBTCExchange.SOCKET_TIMEOUT_PARAMETER);
		final Integer connectTimeout = (Integer) exchangeSpecification
				.getParameter(CHBTCExchange.CONNECT_TIMEOUT_PARAMETER);
		final Integer connectionRequestTimeout = (Integer) exchangeSpecification
				.getParameter(CHBTCExchange.CONNECTION_REQUEST_TIMEOUT_PARAMETER);

		client = new CHBTCClient(
				tradeApiUrl,
				accessKey,
				secretKey,
				socketTimeout == null ? 0 : socketTimeout.intValue(),
				connectTimeout == null ? 0 : connectTimeout.intValue(),
				connectionRequestTimeout == null ? 0
						: connectionRequestTimeout.intValue());
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
