package org.oxerr.chbtc.service.polling;

import org.oxerr.chbtc.CHBTCClient;
import org.oxerr.chbtc.CHBTCExchange;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;

public class CHBTCBaseTradePollingService extends CHBTCBasePollingService {

	protected final CHBTCClient client;

	protected CHBTCBaseTradePollingService(Exchange exchange) {
		super(exchange);
		ExchangeSpecification spec = exchange.getExchangeSpecification();

		final String tradeApiUrl = spec.getSslUri();
		final String accessKey = spec.getApiKey();
		final String secretKey = spec.getSecretKey();

		final Integer socketTimeout = (Integer) spec
				.getParameter(CHBTCExchange.SOCKET_TIMEOUT_PARAMETER);
		final Integer connectTimeout = (Integer) spec
				.getParameter(CHBTCExchange.CONNECT_TIMEOUT_PARAMETER);
		final Integer connectionRequestTimeout = (Integer) spec
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
