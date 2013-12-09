package com.redv.chbtc.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.redv.chbtc.domain.EntrustDetail;
import com.redv.chbtc.domain.Type;

public class EntrustDetailsParserTest {

	private EntrustDetailsParser parser = new EntrustDetailsParser();

	@Test
	public void testParse() throws IOException, SAXException, ParseException {
		try (InputStream inputStream = getClass().getResourceAsStream("EntrustDetails.html")) {
			List<EntrustDetail> details = parser.parse(inputStream);
			assertEquals(10, details.size());

			EntrustDetail detail = details.get(0);
			assertEquals("201312071044266", detail.getId());
			assertEquals(Type.BUY, detail.getType());
			assertEquals(new BigDecimal("4903.00"), detail.getPrice());
			assertEquals(new BigDecimal("4903.0"), detail.getAvgPrice());
			assertEquals(new BigDecimal("0.0010"), detail.getAmount());
			assertEquals(new BigDecimal("0.0010"), detail.getFilledAmount());
			assertEquals(new BigDecimal("4.903"), detail.getTotal());
			assertEquals(new BigDecimal("4.903"), detail.getFilled());
		}
	}

}
