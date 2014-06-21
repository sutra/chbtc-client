package com.redv.chbtc;

import java.util.Arrays;
import java.util.List;

import com.redv.chbtc.service.polling.CHBTCMarketDataService;
import com.xeiam.xchange.BaseExchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;

public class CHBTCExchange extends BaseExchange {

	/**
	 * The parameter name of the symbols that will focus on.
	 */
	public static final String SYMBOLS_PARAMETER = "symbols";

	private static final List<CurrencyPair> SYMBOLS = Arrays.asList(
			CurrencyPair.BTC_CNY,
			CurrencyPair.LTC_CNY);

	@Override
	public void applySpecification(ExchangeSpecification exchangeSpecification) {
		super.applySpecification(exchangeSpecification);
		this.pollingMarketDataService = new CHBTCMarketDataService(exchangeSpecification);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExchangeSpecification getDefaultExchangeSpecification() {
		ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
		exchangeSpecification.setSslUri("https://trade.chbtc.com/api");
		exchangeSpecification.setPlainTextUri("http://api.chbtc.com/data");
		exchangeSpecification.setExchangeName("CHBTC");
		exchangeSpecification.setExchangeSpecificParametersItem(
				SYMBOLS_PARAMETER, SYMBOLS);
		return exchangeSpecification;
	}

}
