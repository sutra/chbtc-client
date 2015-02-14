package org.oxerr.chbtc.service.polling;

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

import si.mazi.rescu.RestProxyFactory;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.currency.CurrencyPair;

public class CHBTCMarketDataServiceRaw extends CHBTCBasePollingService {

	private final Map<String, CHBTCMarketData> chbtcMarketDatas = new HashMap<>(2);

	protected CHBTCMarketDataServiceRaw(Exchange exchange) {
		super(exchange);
		ExchangeSpecification spec = exchange.getExchangeSpecification();
		final String baseUrl = spec.getPlainTextUri();
		final Collection<CurrencyPair> exchangeSymbols;
		try {
			exchangeSymbols = getExchangeSymbols();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (exchangeSymbols.contains(CurrencyPair.BTC_CNY)) {
			chbtcMarketDatas.put(Currencies.BTC,
					RestProxyFactory.createProxy(
							CHBTCMarketDataBTC.class, baseUrl));
		}
		if (exchangeSymbols.contains(CurrencyPair.LTC_CNY)) {
			chbtcMarketDatas.put(Currencies.LTC,
					RestProxyFactory.createProxy(
							CHBTCMarketDataLTC.class, baseUrl));
		}
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
		return chbtcMarketDatas.get(currencyPair.baseSymbol);
	}

}
