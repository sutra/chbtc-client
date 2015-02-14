package org.oxerr.chbtc.service.polling;

import java.io.IOException;
import java.util.List;

import org.oxerr.chbtc.CHBTCExchange;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.service.polling.BasePollingExchangeService;
import com.xeiam.xchange.service.polling.BasePollingService;

public class CHBTCBasePollingService extends BasePollingExchangeService
		implements BasePollingService {

	private final List<CurrencyPair> symbols;

	@SuppressWarnings("unchecked")
	protected CHBTCBasePollingService(Exchange exchange) {
		super(exchange);
		ExchangeSpecification spec = exchange.getExchangeSpecification();
		symbols = (List<CurrencyPair>) spec
				.getExchangeSpecificParametersItem(
						CHBTCExchange.SYMBOLS_PARAMETER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CurrencyPair> getExchangeSymbols() throws IOException {
		return symbols;
	}

}
