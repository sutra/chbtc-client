package com.redv.chbtc.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParserTest {

	private ObjectMapper mapper = new ObjectMapper(); // create once, reuse

	@Test
	public void testParseTicker() throws JsonParseException,
			JsonMappingException, IOException {
		Ticker ticker = mapper.readValue(getClass().getResource("ticker.json"),
				TickerResponse.class).getTicker();
		assertEquals(new BigDecimal("5218.01"), ticker.getBuy());
	}

	@Test
	public void testParseDepth() throws JsonParseException, JsonMappingException, IOException {
		Depth depth = mapper.readValue(getClass().getResource("depth.json"), Depth.class);
		assertEquals(new BigDecimal("5250.00"), depth.getAsks().get(0).getRate());
	}

	@Test
	public void testParseTrades() throws JsonParseException, JsonMappingException, IOException {
		List<Trade> trades = mapper.readValue(
				getClass().getResource("trades.json"),
				new TypeReference<List<Trade>>() {
				});
		assertEquals(80, trades.size());
	}

	@Test
	public void testParseLog() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);
		Unmarshaller um = jaxbContext.createUnmarshaller();
		Root root = (Root) um.unmarshal(getClass().getResource("login.xml"));
		assertEquals("https://www.chbtc.com", root.getTitle());
		assertTrue(root.isSuccess());
		assertEquals("https://www.chbtc.com", root.getDes());
		assertEquals("https://www.chbtc.com", root.getMainData());
	}
}
