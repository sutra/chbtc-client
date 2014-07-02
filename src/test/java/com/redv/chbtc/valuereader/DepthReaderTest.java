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
			assertEquals(new BigDecimal("4388.00"), depth.getAsks()[0][0]);
			assertEquals(new BigDecimal("4380.00"), depth.getAsks()[1][0]);

			assertEquals(new BigDecimal("4210.00"), depth.getBids()[0][0]);
			assertEquals(new BigDecimal("4209.00"), depth.getBids()[1][0]);
		}
	}

}
