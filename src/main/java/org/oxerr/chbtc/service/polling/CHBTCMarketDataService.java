package org.oxerr.chbtc.service.polling;

import java.io.IOException;

import org.oxerr.chbtc.CHBTCAdapters;
import org.oxerr.chbtc.dto.Trade;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.exceptions.ExchangeException;
import com.xeiam.xchange.exceptions.NotAvailableFromExchangeException;
import com.xeiam.xchange.exceptions.NotYetImplementedForExchangeException;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;

public class CHBTCMarketDataService extends CHBTCMarketDataServiceRaw implements
		PollingMarketDataService {

	public CHBTCMarketDataService(Exchange exchange) {
		super(exchange);
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

}
