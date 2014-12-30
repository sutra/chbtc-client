package com.redv.chbtc.domain;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

public class DepthTest extends UnmarshalTest {

	@Test
	public void testUnmarshal() throws IOException {
		Depth depth = readValue("depth.json", Depth.class);

		// asks
		assertEquals(new BigDecimal("1918.78"), depth.getAsks()[0][0]);
		assertEquals(new BigDecimal("0.041"), depth.getAsks()[0][1]);

		assertEquals(new BigDecimal("1919.0"), depth.getAsks()[1][0]);
		assertEquals(new BigDecimal("0.01"), depth.getAsks()[1][1]);

		// bids
		assertEquals(new BigDecimal("1915.0"), depth.getBids()[0][0]);
		assertEquals(new BigDecimal("0.009"), depth.getBids()[0][1]);

		assertEquals(new BigDecimal("1913.0"), depth.getBids()[1][0]);
		assertEquals(new BigDecimal("0.07"), depth.getBids()[1][1]);
	}

}
