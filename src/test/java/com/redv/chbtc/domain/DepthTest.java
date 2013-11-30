package com.redv.chbtc.domain;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import com.redv.chbtc.domain.Depth.Data;

public class DepthTest {

	@Test
	public void testSort0() {
		Depth depth = new Depth();

		depth.setBids(new BigDecimal[][] {
				{ new BigDecimal(1), new BigDecimal(1) },
				{ new BigDecimal(2), new BigDecimal(1) } });
		depth.setAsks(new BigDecimal[][] {
				{ new BigDecimal(1), new BigDecimal(1) },
				{ new BigDecimal(2), new BigDecimal(1) } });

		assertEquals(new BigDecimal(2), depth.getBids().get(0).getRate());
		assertEquals(new BigDecimal(1), depth.getAsks().get(0).getRate());
	}

	@Test
	public void testSort1() {
		Depth depth = new Depth();

		depth.setBids(new BigDecimal[][] {
				{ new BigDecimal(2), new BigDecimal(1) },
				{ new BigDecimal(1), new BigDecimal(1) } });
		depth.setAsks(new BigDecimal[][] {
				{ new BigDecimal(2), new BigDecimal(1) },
				{ new BigDecimal(1), new BigDecimal(1) } });

		assertEquals(new BigDecimal(2), depth.getBids().get(0).getRate());
		assertEquals(new BigDecimal(1), depth.getAsks().get(0).getRate());
	}

	@Test
	public void testDataCompare() {
		Data bid0 = new Data(Type.BUY, new BigDecimal(2), new BigDecimal(1));
		Data bid1 = new Data(Type.BUY, new BigDecimal(2), new BigDecimal(1));

		assertTrue(bid0.compareTo(bid1) == 0);

		bid0 = new Data(Type.BUY, new BigDecimal(2), new BigDecimal(1));
		bid1 = new Data(Type.BUY, new BigDecimal(1), new BigDecimal(1));

		assertTrue(bid0.compareTo(bid1) < 0);

		Data ask0 = new Data(Type.SELL, new BigDecimal(2), new BigDecimal(1));
		Data ask1 = new Data(Type.SELL, new BigDecimal(2), new BigDecimal(1));

		assertTrue(ask0.compareTo(ask1) == 0);

		ask0 = new Data(Type.SELL, new BigDecimal(1), new BigDecimal(1));
		ask1 = new Data(Type.SELL, new BigDecimal(2), new BigDecimal(1));

		assertTrue(ask0.compareTo(ask1) < 0);
	}

}
