package com.redv.chbtc.service.polling;

import java.io.IOException;
import java.util.Collection;

import com.redv.chbtc.CHBTCExchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.service.polling.BasePollingExchangeService;

public class CHBTCBasePollingService extends BasePollingExchangeService {

	private final Collection<CurrencyPair> symbols;

	@SuppressWarnings("unchecked")
	protected CHBTCBasePollingService(
			ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
		symbols = (Collection<CurrencyPair>) exchangeSpecification
				.getExchangeSpecificParametersItem(
						CHBTCExchange.SYMBOLS_PARAMETER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<CurrencyPair> getExchangeSymbols() throws IOException {
		return symbols;
	}

}
