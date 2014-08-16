package com.redv.chbtc;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redv.chbtc.domain.Order;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.xeiam.xchange.dto.marketdata.Trades;

public class CHBTCAdaptersTest {

	private final ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testAdaptTradesOrder() throws IOException {
		Order order = mapper.readValue(getClass().getResource("domain/order.json"), Order.class);
		Trades trades = CHBTCAdapters.adaptTrades(order, 8);
		assertEquals(1, trades.getTrades().size());
		Trade trade = trades.getTrades().get(0);
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
