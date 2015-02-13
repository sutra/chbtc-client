package com.redv.chbtc.service.polling;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import si.mazi.rescu.RestProxyFactory;

import com.redv.chbtc.CHBTCMarketData;
import com.redv.chbtc.CHBTCMarketDataBTC;
import com.redv.chbtc.CHBTCMarketDataLTC;
import com.redv.chbtc.domain.Depth;
import com.redv.chbtc.domain.TickerResponse;
import com.redv.chbtc.domain.Trade;
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
