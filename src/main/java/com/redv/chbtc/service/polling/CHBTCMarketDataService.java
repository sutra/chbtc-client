package com.redv.chbtc.service.polling;

import java.io.IOException;
import java.util.ArrayList;

import com.redv.chbtc.CHBTCAdapters;
import com.redv.chbtc.domain.Trade;
import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.NotAvailableFromExchangeException;
import com.xeiam.xchange.NotYetImplementedForExchangeException;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.ExchangeInfo;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.service.polling.PollingMarketDataService;

public class CHBTCMarketDataService extends CHBTCMarketDataServiceRaw implements
		PollingMarketDataService {

	/**
	 * Constructor.
	 * 
	 * @param exchangeSpecification The {@link ExchangeSpecification}.
	 */
	public CHBTCMarketDataService(ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ticker getTicker(CurrencyPair currencyPair, Object... args)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		return CHBTCAdapters.adaptTicker(getTicker(currencyPair), currencyPair);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		return CHBTCAdapters.adaptOrderBook(getDepth(currencyPair), currencyPair);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Trades getTrades(CurrencyPair currencyPair, Object... args)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		final Trade[] trades;
		if (args.length == 0) {
			trades = getTrades(currencyPair);
		} else {
			trades = getTrades(currencyPair, (Long) args[0]);
		}
		return CHBTCAdapters.adaptTrades(trades, currencyPair);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExchangeInfo getExchangeInfo() throws ExchangeException,
			IOException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException {
		return new ExchangeInfo(new ArrayList<>(getExchangeSymbols()));
	}

}