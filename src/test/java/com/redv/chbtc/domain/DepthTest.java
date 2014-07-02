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
		assertEquals(new BigDecimal("5356.00"), depth.getAsks()[0][0]);
		assertEquals(new BigDecimal("0.1000"), depth.getAsks()[0][1]);

		assertEquals(new BigDecimal("5355.00"), depth.getAsks()[1][0]);
		assertEquals(new BigDecimal("0.5290"), depth.getAsks()[1][1]);

		// bids
		assertEquals(new BigDecimal("5248.00"), depth.getBids()[0][0]);
		assertEquals(new BigDecimal("0.0010"), depth.getBids()[0][1]);

		assertEquals(new BigDecimal("5240.00"), depth.getBids()[1][0]);
		assertEquals(new BigDecimal("0.0100"), depth.getBids()[1][1]);
	}

}
