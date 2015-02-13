package com.redv.chbtc;

import java.util.Arrays;
import java.util.List;

import si.mazi.rescu.SynchronizedValueFactory;

import com.redv.chbtc.service.polling.CHBTCAccountService;
import com.redv.chbtc.service.polling.CHBTCMarketDataService;
import com.redv.chbtc.service.polling.CHBTCTradeService;
import com.xeiam.xchange.BaseExchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;

public class CHBTCExchange extends BaseExchange {

	/**
	 * The parameter name of the symbols that will focus on.
	 */
	public static final String SYMBOLS_PARAMETER = "symbols";

	public static final String SOCKET_TIMEOUT_PARAMETER = "socketTimeout";
	public static final String CONNECT_TIMEOUT_PARAMETER = "connectTimeout";
	public static final String CONNECTION_REQUEST_TIMEOUT_PARAMETER = "connectionRequestTimeout";

	public static final String PRICE_SCALE_PARAMETER = "priceScale";

	private static final List<CurrencyPair> SYMBOLS = Arrays.asList(
			CurrencyPair.BTC_CNY,
			CurrencyPair.LTC_CNY);

	@Override
	public void applySpecification(ExchangeSpecification exchangeSpecification) {
		super.applySpecification(exchangeSpecification);
		this.pollingMarketDataService = new CHBTCMarketDataService(this);
		if (exchangeSpecification.getApiKey() != null) {
			this.pollingAccountService = new CHBTCAccountService(this);
			this.pollingTradeService = new CHBTCTradeService(this);
		}
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
		exchangeSpecification.setExchangeSpecificParametersItem(
				PRICE_SCALE_PARAMETER, 8);
		return exchangeSpecification;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SynchronizedValueFactory<Long> getNonceFactory() {
		return null;
	}

}
