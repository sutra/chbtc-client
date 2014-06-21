package com.redv.chbtc.service.polling;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.message.BasicNameValuePair;

import com.redv.chbtc.CHBTC;
import com.redv.chbtc.CHBTCClient;
import com.redv.chbtc.domain.CHBTCError;
import com.redv.chbtc.domain.Order;
import com.redv.chbtc.domain.OrderResponse;
import com.redv.chbtc.domain.Type;
import com.xeiam.xchange.ExchangeSpecification;

public class CHBTCTradeServiceRaw extends CHBTCBaseTradePollingService {

	private static final Order[] ORDER_ARRAY = new Order[] {};

	private final CHBTCClient client;

	/**
	 * @param exchangeSpecification
	 */
	protected CHBTCTradeServiceRaw(ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);

		final String secretKey = exchangeSpecification.getSecretKey();
		final int socketTimeout = NumberUtils.toInt((String) exchangeSpecification.getParameter("socketTimeout"));
		final int connectTimeout = NumberUtils.toInt((String) exchangeSpecification.getParameter("connectTimeout"));
		final int connectionRequestTimeout = NumberUtils.toInt((String) exchangeSpecification.getParameter("connectionRequestTimeout"));

		client = new CHBTCClient(
				accessKey,
				secretKey,
				socketTimeout,
				connectTimeout,
				connectionRequestTimeout);
	}

	public OrderResponse order(BigDecimal price, BigDecimal amount,
			Type tradeType, String currency) {
		String priceString = price.toPlainString();
		String amountString = amount.toPlainString();
		int tradeTypeInt = tradeType.getTradeType();

		String sign = sign(
				CHBTC.METHOD_ORDER,
				new BasicNameValuePair("price", priceString),
				new BasicNameValuePair("amount", amountString),
				new BasicNameValuePair("tradeType", String.valueOf(tradeTypeInt)),
				new BasicNameValuePair("currency", currency));

		return chbtc.order(
				CHBTC.METHOD_ORDER,
				accessKey,
				priceString,
				amountString,
				tradeTypeInt,
				currency,
				sign,
				getReqTime());
	}

	public CHBTCError cancelOrder(long id, String currency) {
		String sign = sign(
				CHBTC.METHOD_CANCEL_ORDER,
				new BasicNameValuePair("id", String.valueOf(id)),
				new BasicNameValuePair("currency", currency));

		return chbtc.cancelOrder(
				CHBTC.METHOD_CANCEL_ORDER,
				accessKey,
				id,
				currency,
				sign,
				getReqTime());
	}

	public Order getOrder(long id, String currency) {
		String sign = sign(
				CHBTC.METHOD_GET_ORDER,
				new BasicNameValuePair("id", String.valueOf(id)),
				new BasicNameValuePair("currency", currency));

		return chbtc.getOrder(
				CHBTC.METHOD_GET_ORDER,
				accessKey,
				id,
				currency,
				sign,
				getReqTime());
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
