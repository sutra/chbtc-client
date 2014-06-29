package com.redv.chbtc.service.polling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.redv.chbtc.CHBTCAdapters;
import com.redv.chbtc.CHBTCClientException;
import com.redv.chbtc.domain.CHBTCError;
import com.redv.chbtc.domain.Order;
import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.NotAvailableFromExchangeException;
import com.xeiam.xchange.NotYetImplementedForExchangeException;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.service.polling.PollingTradeService;

public class CHBTCTradeService extends CHBTCTradeServiceRaw implements
		PollingTradeService {

	private static final int MAXIMUM_PAGE_SIZE = 100;

	/**
	 * @param exchangeSpecification
	 */
	public CHBTCTradeService(ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OpenOrders getOpenOrders() throws ExchangeException,
			NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		List<LimitOrder> openOrders = new ArrayList<>();

		for (CurrencyPair currencyPair : getExchangeSymbols()) {
			String currency = CHBTCAdapters.adaptCurrency(currencyPair);
			int pageIndex = 0;
			Order[] orders;
			do {
				orders = getUnfinishedOrdersIgnoreTradeType(
						currency,
						pageIndex,
						MAXIMUM_PAGE_SIZE);
				openOrders.addAll(CHBTCAdapters.adaptLimitOrders(orders));

				pageIndex++;
			} while (orders.length == MAXIMUM_PAGE_SIZE);
		}

		return new OpenOrders(openOrders);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String placeMarketOrder(MarketOrder marketOrder)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		throw new NotAvailableFromExchangeException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String placeLimitOrder(LimitOrder limitOrder)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		long orderId = order(
				limitOrder.getLimitPrice(),
				limitOrder.getTradableAmount(),
				CHBTCAdapters.adaptType(limitOrder.getType()),
				CHBTCAdapters.adaptCurrency(limitOrder.getCurrencyPair()));
		return String.valueOf(orderId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean cancelOrder(String orderId) throws ExchangeException,
			NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		boolean success = false;

		for (CurrencyPair currencyPair : getExchangeSymbols()) {
			try {
				cancelOrder(Long.parseLong(orderId),
						CHBTCAdapters.adaptCurrency(currencyPair));
				success = true;
			} catch (CHBTCClientException e) {
				if (e.getErrorCode() == CHBTCError.NOT_FOUND_ORDER) {
					continue;
				} else {
					throw e;
				}
			}
		}

		return success;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Trades getTradeHistory(Object... arguments)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		if (arguments.length < 3) {
			throw new IllegalArgumentException(
					"3 arguments required: currency pair, page index(1 based), page size.");
		}

		CurrencyPair currencyPair = (CurrencyPair) arguments[0];
		int pageIndex = (Integer) arguments[1];
		int pageSize = (Integer) arguments[2];

		if (pageSize > MAXIMUM_PAGE_SIZE) {
			throw new IllegalArgumentException(
					String.format("Page size %1$d is greater than maximum page size %2$d.",
							pageSize,
							MAXIMUM_PAGE_SIZE));
		}

		Order[] orders = getOrdersIgnoreTradeType(
				CHBTCAdapters.adaptCurrency(currencyPair),
				pageIndex,
				pageSize);

		return CHBTCAdapters.adaptTrades(orders);
	}

}
