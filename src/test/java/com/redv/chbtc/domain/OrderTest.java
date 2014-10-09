package com.redv.chbtc.domain;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderTest {
	
	private final ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testOrder() throws IOException {
		Order order = mapper.readValue(getClass().getResource("order.json"),
				Order.class);
		assertEquals("btc", order.getCurrency());
		assertEquals(2014050277474869L, order.getId());
		assertEquals(new BigDecimal("2760.01"), order.getPrice());
		assertEquals(2, order.getStatus());
		assertEquals(new BigDecimal("0.019"), order.getTotalAmount());
		assertEquals(new BigDecimal("0.019"), order.getTradeAmount());
		assertEquals(1399037330321L, order.getTradeDate());
		assertEquals(new BigDecimal("52.45009"), order.getTradeMoney());
		assertEquals(0, order.getType());
	}

}
