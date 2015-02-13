package org.oxerr.chbtc.service.polling;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.oxerr.chbtc.dto.Order;
import org.oxerr.chbtc.dto.Type;

import com.xeiam.xchange.Exchange;

public class CHBTCTradeServiceRaw extends CHBTCBaseTradePollingService {

	private static final Order[] ORDER_ARRAY = new Order[] {};

	protected CHBTCTradeServiceRaw(Exchange exchange) {
		super(exchange);
	}

	/**
	 * Place order.
	 *
	 * @param price the order price.
	 * @param amount the order amount.
	 * @param tradeType the trade type, buy or sell.
	 * @param currency the base symbol in lower case to specifiy market.
	 * e.g. btc, ltc.
	 * @return the order ID.
	 * @throws IOException indicates I/O exception.
	 */
	public long order(BigDecimal price, BigDecimal amount,
			Type tradeType, String currency) throws IOException {
		return client.order(price, amount, tradeType, currency);
	}

	/**
	 * Cancel the specified order.
	 *
	 * @param id the order ID.
	 * @param currency the base symbol in lower case to specify the market.
	 * e.g. btc, ltc.
	 * @throws IOException indicates I/O exception.
	 */
	public void cancelOrder(long id, String currency) throws IOException {
		client.cancelOrder(id, currency);
	}

	/**
	 * Returns order with given order ID in the specified market.
	 *
	 * @param id the order ID.
	 * @param currency the base symbol in lower case to specify market.
	 * e.g. btc, ltc.
	 * @return the order with given order ID in the specified market.
	 * @throws IOException indicates I/O exception.
	 */
	public Order getOrder(long id, String currency) throws IOException {
		return client.getOrder(id, currency);
	}

	/**
	 * Returns bid or ask orders, 10 records maximumly.
	 *
	 * @param tradeType the trade type, buy or sell.
	 * @param currency base symbol in lower case. e.g. btc, ltc.
	 * @param pageIndex 1 based.
	 * @return bid or ask orders.
	 * @throws IOException indicates I/O exception.
	 */
	public Order[] getOrders(Type tradeType, String currency, int pageIndex)
			throws IOException {
		List<Order> orderList = client.getOrders(tradeType, currency, pageIndex);
		return orderList.toArray(ORDER_ARRAY);
	}

	/**
	 * Returns bid or ask orders.
	 *
	 * @param tradeType the trade type, buy or sell.
	 * @param currency the base symbol in lower case to specify market.
	 * e.g. btc, ltc.
	 * @param pageIndex 1 based.
	 * @param pageSize should not greater than 100.
	 * @return bid or ask orders.
	 * @throws IOException indicates I/O exception.
	 */
	public Order[] getOrdersNew(Type tradeType, String currency, int pageIndex,
			int pageSize) throws IOException {
		List<Order> orderList = client.getOrdersNew(
				tradeType, currency, pageIndex, pageSize);
		return orderList.toArray(ORDER_ARRAY);
	}

	/**
	 * Returns bid or ask orders.
	 *
	 * @param currency the base symbol in lower case to specify market.
	 * e.g. btc, ltc.
	 * @param pageIndex 1 based.
	 * @param pageSize should not greater than 100.
	 * @return bid or ask orders.
	 * @throws IOException indicates I/O exception.
	 */
	public Order[] getOrdersIgnoreTradeType(String currency, int pageIndex,
			int pageSize) throws IOException {
		List<Order> orderList = client.getOrdersIgnoreTradeType(
				currency, pageIndex, pageSize);
		return orderList.toArray(ORDER_ARRAY);
	}

	/**
	 * Returns bid or ask orders.
	 *
	 * @param currency the base symbol in lower case to specify market.
	 * e.g. btc, ltc.
	 * @param pageIndex 1 based.
	 * @param pageSize should not greater than 100.
	 * @return bid or ask orders.
	 * @throws IOException indicates I/O exception.
	 */
	public Order[] getUnfinishedOrdersIgnoreTradeType(String currency,
			int pageIndex, int pageSize) throws IOException {
		List<Order> orderList = client.getUnfinishedOrdersIgnoreTradeType(
				currency,
				pageIndex,
				pageSize);
		return orderList.toArray(ORDER_ARRAY);
	}

}
