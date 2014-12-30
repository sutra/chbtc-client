package com.redv.chbtc;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redv.chbtc.domain.Depth;
import com.redv.chbtc.domain.Order;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.UserTrade;
import com.xeiam.xchange.dto.trade.UserTrades;

public class CHBTCAdaptersTest {

	private final ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testAdpatOrderBook() throws IOException {
		Depth depth = mapper.readValue(getClass().getResource("domain/depth.json"), Depth.class);
		OrderBook orderBook = CHBTCAdapters.adaptOrderBook(depth, CurrencyPair.BTC_CNY);

		List<LimitOrder> asks = orderBook.getAsks();
		List<LimitOrder> bids = orderBook.getBids();

		// asks should be sorted ascending
		// ask 0.041@1918.78
		assertEquals(new BigDecimal("1918.78"), asks.get(0).getLimitPrice());
		assertEquals(new BigDecimal("0.041"), asks.get(0).getTradableAmount());

		// ask 0.01@1919.0
		assertEquals(new BigDecimal("1919.0"), asks.get(1).getLimitPrice());
		assertEquals(new BigDecimal("0.01"), asks.get(1).getTradableAmount());

		// bids should be sorted descending
		// bid 0.009@1915.0
		assertEquals(new BigDecimal("1915.0"), bids.get(0).getLimitPrice());
		assertEquals(new BigDecimal("0.009"), bids.get(0).getTradableAmount());

		// bid 0.07@1913.0
		assertEquals(new BigDecimal("1913.0"), bids.get(1).getLimitPrice());
		assertEquals(new BigDecimal("0.07"), bids.get(1).getTradableAmount());
	}

	@Test
	public void testAdaptTradesOrder() throws IOException {
		Order order = mapper.readValue(getClass().getResource("domain/order.json"), Order.class);
		UserTrades trades = CHBTCAdapters.adaptUserTrades(order, 8);
		assertEquals(1, trades.getTrades().size());
		UserTrade trade = trades.getUserTrades().get(0);
		assertEquals(CurrencyPair.BTC_CNY, trade.getCurrencyPair());
		assertEquals("2014050277474869", trade.getOrderId());
		assertEquals(new BigDecimal("2760.53105263"), trade.getPrice());
		assertEquals(new BigDecimal("0.019"), trade.getTradableAmount());
		assertEquals(1399037330321L, trade.getTimestamp().getTime());
		assertEquals(OrderType.ASK, trade.getType());
	}

	@Test
	public void testDivide() {
		BigDecimal result = new BigDecimal("52").divide(
				new BigDecimal("0.019"), RoundingMode.HALF_EVEN);
		assertEquals(new BigDecimal("2737"), result);

		result = new BigDecimal("52").divide(
				new BigDecimal("0.019"), 8, RoundingMode.HALF_EVEN);
		assertEquals(new BigDecimal("2736.84210526"), result);
	}
}
