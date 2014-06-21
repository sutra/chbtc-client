package com.redv.chbtc.service.polling;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.redv.chbtc.domain.Order;
import com.redv.chbtc.domain.Type;
import com.xeiam.xchange.ExchangeSpecification;

public class CHBTCTradeServiceRaw extends CHBTCBaseTradePollingService {

	private static final Order[] ORDER_ARRAY = new Order[] {};

	/**
	 * @param exchangeSpecification
	 */
	protected CHBTCTradeServiceRaw(ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
	}

	public long order(BigDecimal price, BigDecimal amount,
			Type tradeType, String currency) throws IOException {
		return client.order(price, amount, tradeType, currency);
	}

	public void cancelOrder(long id, String currency) throws IOException {
		client.cancelOrder(id, currency);
	}

	public Order getOrder(long id, String currency) throws IOException {
		return client.getOrder(id, currency);
	}

	public Order[] getOrders(Type tradeType, String currency, int pageIndex)
			throws IOException {
		List<Order> orderList = client.getOrders(tradeType, currency, pageIndex);
		return orderList.toArray(ORDER_ARRAY);
	}

	public Order[] getOrdersNew(Type tradeType, String currency, int pageIndex,
			int pageSize) throws IOException {
		List<Order> orderList = client.getOrdersNew(
				tradeType, currency, pageIndex, pageSize);
		return orderList.toArray(ORDER_ARRAY);
	}

	public Order[] getOrdersIgnoreTradeType(String currency, int pageIndex,
			int pageSize) throws IOException {
		List<Order> orderList = client.getOrdersIgnoreTradeType(
				currency, pageIndex, pageSize);
		return orderList.toArray(ORDER_ARRAY);
	}

	public Order[] getUnfinishedOrdersIgnoreTradeType(String currency,
			int pageIndex, int pageSize) throws IOException {
		List<Order> orderList = client.getUnfinishedOrdersIgnoreTradeType(
				currency,
				pageIndex,
				pageSize);
		return orderList.toArray(ORDER_ARRAY);
	}

}
