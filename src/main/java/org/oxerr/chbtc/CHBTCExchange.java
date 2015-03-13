package org.oxerr.chbtc;

import java.util.Arrays;
import java.util.List;

import org.oxerr.chbtc.service.polling.CHBTCAccountService;
import org.oxerr.chbtc.service.polling.CHBTCMarketDataService;
import org.oxerr.chbtc.service.polling.CHBTCTradeService;

import si.mazi.rescu.SynchronizedValueFactory;

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

	public static final String ALTERNATIVE_DATA_URI_PARAMETER = "alternativeDataUri";
	public static final String CHECKER_LIFECYCLE_MILLIS_PARAMETER = "checkerLifecycleMillis";
	public static final String CHECK_INTERVAL_MILLIS_PARAMETER = "checkIntervalMillis";

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
		exchangeSpecification.setExchangeSpecificParametersItem(
			ALTERNATIVE_DATA_URI_PARAMETER, "https://trans.chbtc.com/data");
		exchangeSpecification.setExchangeSpecificParametersItem(
			CHECKER_LIFECYCLE_MILLIS_PARAMETER, 300_000);
		exchangeSpecification.setExchangeSpecificParametersItem(
			CHECK_INTERVAL_MILLIS_PARAMETER, 60_000);
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
