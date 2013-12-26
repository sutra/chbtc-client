package com.redv.chbtc.valuereader;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;

import com.redv.chbtc.domain.EntrustDetail;
import com.redv.chbtc.domain.Status;
import com.redv.chbtc.domain.Type;

public class EntrustDetailsReaderTest {

	private EntrustDetailsReader parser = new EntrustDetailsReader();

	@Test
	public void testParseAll() throws IOException {
		try (InputStream inputStream = getClass().getResourceAsStream("EntrustDetails-all.html")) {
			List<EntrustDetail> details = parser.read(inputStream);
			assertEquals(10, details.size());

			EntrustDetail detail = details.get(0);
			assertEquals("2013-12-26 20:28:18", DateFormatUtils.format(detail.getDate(), "yyyy-MM-dd HH:mm:ss"));
			assertEquals(Type.SELL, detail.getType());
			assertEquals(new BigDecimal("4140.00"), detail.getPrice());
			assertEquals(new BigDecimal("4140.0"), detail.getAvgPrice());
			assertEquals(new BigDecimal("0.0070"), detail.getAmount());
			assertEquals(new BigDecimal("0.0070"), detail.getFilledAmount());
			assertEquals(new BigDecimal("28.98"), detail.getTotal());
			assertEquals(new BigDecimal("28.98"), detail.getFilled());
			assertEquals("201312261505578", detail.getId());
		}
	}

	@Test
	public void testParseBuying() throws IOException {
		try (InputStream inputStream = getClass().getResourceAsStream("EntrustDetails-buying.html")) {
			List<EntrustDetail> details = parser.read(inputStream);
			assertEquals(1, details.size());

			EntrustDetail detail = details.get(0);
			assertEquals("2013-12-27 00:35:40", DateFormatUtils.format(detail.getDate(), "yyyy-MM-dd HH:mm:ss"));
			assertEquals(Type.SELL, detail.getType());
			assertEquals(new BigDecimal("4300.0"), detail.getPrice());
			assertEquals(new BigDecimal("0.0"), detail.getAvgPrice());
			assertEquals(new BigDecimal("0.02"), detail.getAmount());
			assertEquals(new BigDecimal("0.0"), detail.getFilledAmount());
			assertEquals(new BigDecimal("86.0"), detail.getTotal());
			assertEquals(new BigDecimal("0.0"), detail.getFilled());
			assertEquals(Status.UNKNOWN, detail.getStatus());
			assertEquals("201312271511273", detail.getId());
		}
	}

}
