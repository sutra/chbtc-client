package org.oxerr.chbtc.dto;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.oxerr.chbtc.dto.Ticker;
import org.oxerr.chbtc.dto.TickerResponse;
import org.oxerr.chbtc.dto.Trade;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ParserTest extends UnmarshalTest {

	@Test
	public void testParseTicker() throws JsonParseException,
			JsonMappingException, IOException {
		Ticker ticker = readValue("ticker.json", TickerResponse.class)
				.getTicker();
		assertEquals(new BigDecimal("5218.01"), ticker.getBuy());
	}

	@Test
	public void testParseTrades() throws JsonParseException, JsonMappingException, IOException {
		List<Trade> trades = readValue(
				"trades.json",
				new TypeReference<List<Trade>>() {
				});
		assertEquals(80, trades.size());
	}

}
