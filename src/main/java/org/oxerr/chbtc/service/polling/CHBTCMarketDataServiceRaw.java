package org.oxerr.chbtc.service.polling;

import static org.oxerr.chbtc.CHBTCExchange.ALTERNATIVE_DATA_URI_PARAMETER;
import static org.oxerr.chbtc.CHBTCExchange.CHECKER_LIFECYCLE_MILLIS_PARAMETER;
import static org.oxerr.chbtc.CHBTCExchange.CHECK_INTERVAL_MILLIS_PARAMETER;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.oxerr.chbtc.CHBTCMarketData;
import org.oxerr.chbtc.CHBTCMarketDataBTC;
import org.oxerr.chbtc.CHBTCMarketDataLTC;
import org.oxerr.chbtc.dto.Depth;
import org.oxerr.chbtc.dto.TickerResponse;
import org.oxerr.chbtc.dto.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import si.mazi.rescu.RestProxyFactory;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.currency.CurrencyPair;

public class CHBTCMarketDataServiceRaw extends CHBTCBasePollingService {


	private final Logger log = LoggerFactory.getLogger(CHBTCMarketDataServiceRaw.class);

	private final Map<String, CHBTCMarketData> chbtcMarketDatas = new HashMap<>(2);
	private final Map<String, CHBTCMarketData> alternativeChbtcMarketDatas = new HashMap<>(2);

	private final String checkCurrency;
	private final long checkerLifecycleMillis;
	private final long checkIntervalMillis;

	private volatile Map<String, CHBTCMarketData> reachableMarketDatas = chbtcMarketDatas;
	private volatile Checker checker;
	private volatile long lastUsedTime = System.currentTimeMillis();

	protected CHBTCMarketDataServiceRaw(Exchange exchange) {
		super(exchange);
		ExchangeSpecification spec = exchange.getExchangeSpecification();

		checkerLifecycleMillis = ((Number) spec.getExchangeSpecificParametersItem(
			CHECKER_LIFECYCLE_MILLIS_PARAMETER)).longValue();
		checkIntervalMillis = ((Number) spec.getExchangeSpecificParametersItem(
			CHECK_INTERVAL_MILLIS_PARAMETER)).longValue();

		final String baseUrl = spec.getPlainTextUri();
		final String alternativeBaseUrl = (String) spec
			.getExchangeSpecificParametersItem(ALTERNATIVE_DATA_URI_PARAMETER);
		final Collection<CurrencyPair> exchangeSymbols;
		try {
			exchangeSymbols = getExchangeSymbols();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		String checkCurrency = null;

		if (exchangeSymbols.contains(CurrencyPair.BTC_CNY)) {
			chbtcMarketDatas.put(Currencies.BTC,
				RestProxyFactory.createProxy(
					CHBTCMarketDataBTC.class, baseUrl));
			alternativeChbtcMarketDatas.put(Currencies.BTC,
				RestProxyFactory.createProxy(
					CHBTCMarketDataBTC.class, alternativeBaseUrl));

			if (checkCurrency == null) {
				checkCurrency = Currencies.BTC;
			}
		}
		if (exchangeSymbols.contains(CurrencyPair.LTC_CNY)) {
			chbtcMarketDatas.put(Currencies.LTC,
				RestProxyFactory.createProxy(
					CHBTCMarketDataLTC.class, baseUrl));
			alternativeChbtcMarketDatas.put(Currencies.LTC,
				RestProxyFactory.createProxy(
					CHBTCMarketDataLTC.class, alternativeBaseUrl));

			if (checkCurrency == null) {
				checkCurrency = Currencies.LTC;
			}
		}

		this.checkCurrency = checkCurrency;
	}

	public TickerResponse getTicker(CurrencyPair currencyPair)
			throws IOException {
		return getData(currencyPair).getTicker();
	}

	public Depth getDepth(CurrencyPair currencyPair) throws IOException {
		return getData(currencyPair).getDepth();
	}

	public Trade[] getTrades(CurrencyPair currencyPair) throws IOException {
		return getData(currencyPair).getTrades();
	}

	public Trade[] getTrades(CurrencyPair currencyPair, long since)
			throws IOException {
		return getData(currencyPair).getTrades(since);
	}

	private CHBTCMarketData getData(CurrencyPair currencyPair) {
		this.lastUsedTime = System.currentTimeMillis();

		if (checkCurrency != null && checker == null) {
			checker = new Checker(checkCurrency, checkerLifecycleMillis, checkIntervalMillis);
			checker.start();
			log.debug("Data API checker started.");
		}

		return reachableMarketDatas.get(currencyPair.baseSymbol);
	}

	private class Checker extends Thread {

		private final String testCurrency;
		private final long lifecycleMillis;
		private final long checkIntervalMillis;

		public Checker(String testCurrency, long lifecycleMillis, long checkIntervalMillis) {
			this.testCurrency = testCurrency;
			this.lifecycleMillis = lifecycleMillis;
			this.checkIntervalMillis = checkIntervalMillis;
		}

		@Override
		public void run() {
			while (!this.isInterrupted()
				&& System.currentTimeMillis() - lastUsedTime < lifecycleMillis) {
				try {
					log.debug("Checking...");
					check();

					Thread.sleep(checkIntervalMillis);
				} catch (InterruptedException e) {
					this.interrupt();
				}
			}

			checker = null;
			log.debug("Data API checker end.");
		}

		private void check() throws InterruptedException {
			try {
				chbtcMarketDatas.get(testCurrency).getTicker();

				if (reachableMarketDatas == alternativeChbtcMarketDatas) {
					reachableMarketDatas = chbtcMarketDatas;
					log.info("Normal DATA API is reachable, switched back.");
				}
			} catch (IOException e) {
				reachableMarketDatas = alternativeChbtcMarketDatas;
				log.warn("Normal DATA API is unreachable, switched to alternative DATA API.");
			}
		}
	}

}
