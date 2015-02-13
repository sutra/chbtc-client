package com.redv.chbtc.service.polling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.redv.chbtc.CHBTCAdapters;
import com.redv.chbtc.CHBTCClientException;
import com.redv.chbtc.CHBTCExchange;
import com.redv.chbtc.domain.CHBTCError;
import com.redv.chbtc.domain.Order;
import com.redv.chbtc.domain.Type;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.dto.trade.UserTrades;
import com.xeiam.xchange.exceptions.ExchangeException;
import com.xeiam.xchange.exceptions.NotAvailableFromExchangeException;
import com.xeiam.xchange.exceptions.NotYetImplementedForExchangeException;
import com.xeiam.xchange.service.polling.trade.PollingTradeService;
import com.xeiam.xchange.service.polling.trade.params.TradeHistoryParams;

public class CHBTCTradeService extends CHBTCTradeServiceRaw implements
		PollingTradeService {

	private static final int MAXIMUM_PAGE_SIZE = 100;

	private final int priceScale;

	public CHBTCTradeService(Exchange exchange) {
		super(exchange);
		ExchangeSpecification spec = exchange.getExchangeSpecification();
		Integer priceScale = (Integer) spec.getParameter(
				CHBTCExchange.PRICE_SCALE_PARAMETER);
		this.priceScale = priceScale != null ? priceScale.intValue() : 8;
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
	 * Returns trades of one order or all trades of the user's account.
	 *
	 * @param arguments
	 * <ol>
	 * <li>{@link CurrencyPair}, required, to specified the market.</li>
	 * <li>order ID, can be null.</li>
	 * <li>pageIndex, optional, 1 based.</li>
	 * <li>pageSize, optional.</li>
	 * <li>type, optional.</li>
	 * </ol>
	 */
	@Override
	public UserTrades getTradeHistory(Object... arguments)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		int argc = arguments.length;

		if (argc < 2) {
			throw new IllegalArgumentException(
					"At least 2 arguments are required.");
		}

		final UserTrades trades;

		CurrencyPair currencyPair = (CurrencyPair) arguments[0];
		String currency = CHBTCAdapters.adaptCurrency(currencyPair);
		Long orderId = argc > 1 ? (Long) arguments[1] : null;
		Integer pageIndex = argc > 2 ? (Integer) arguments[2] : null;
		Integer pageSize = argc > 3 ? (Integer) arguments[3] : null;
		Type type = argc > 4 ? (Type) arguments[4] : null;

		if (orderId != null) {
			Order order = getOrder(orderId.longValue(), currency);
			trades = CHBTCAdapters.adaptUserTrades(order, priceScale);
		} else if (pageIndex != null && pageSize != null && type != null) {
			checkPageSize(pageSize);

			Order[] orders = getOrdersNew(type, currency, pageIndex, pageSize);
			trades = CHBTCAdapters.adaptUserTrades(orders, priceScale);
		} else if (pageIndex != null && pageSize != null){
			checkPageSize(pageSize);

			Order[] orders = getOrdersIgnoreTradeType(
					currency,
					pageIndex,
					pageSize);

			trades = CHBTCAdapters.adaptUserTrades(orders, priceScale);
		} else if (pageIndex != null && type != null) {
			Order[] orders = getOrders(type, currency, pageIndex);
			trades = CHBTCAdapters.adaptUserTrades(orders, priceScale);
		} else {
			throw new IllegalArgumentException();
		}

		return trades;
	}

	private void checkPageSize(int pageSize) {
		if (pageSize > MAXIMUM_PAGE_SIZE) {
			throw new IllegalArgumentException(
				String.format("Page size %1$d is greater than maximum page size %2$d.",
					pageSize,
					MAXIMUM_PAGE_SIZE));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserTrades getTradeHistory(TradeHistoryParams params)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		throw new NotYetImplementedForExchangeException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TradeHistoryParams createTradeHistoryParams() {
		return null;
	}

}
