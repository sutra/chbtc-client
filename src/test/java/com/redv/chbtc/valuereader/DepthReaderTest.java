package com.redv.chbtc.valuereader;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redv.chbtc.domain.Depth;

public class DepthReaderTest {

	@Test
	public void test() throws IOException {
		try (final InputStream inputStream = getClass().getResourceAsStream(
				"depth-malformed.json.txt")) {
			Depth depth = new DepthReader(new ObjectMapper()).read(inputStream);
			assertEquals(new BigDecimal("4210.06"), depth.getAsks().get(0).getRate());
			assertEquals(new BigDecimal("4211.00"), depth.getAsks().get(1).getRate());

			assertEquals(new BigDecimal("4210.00"), depth.getBids().get(0).getRate());
			assertEquals(new BigDecimal("4209.00"), depth.getBids().get(1).getRate());
		}
	}

}
