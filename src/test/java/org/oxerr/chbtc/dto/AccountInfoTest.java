package org.oxerr.chbtc.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oxerr.chbtc.dto.AccountInfo;
import org.oxerr.chbtc.dto.valuereader.JsonValueReader;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AccountInfoTest {

	private static JsonValueReader<AccountInfo> accountInfoReader;

	@BeforeClass
	public static void setUpBeforeClass() {
		accountInfoReader = new JsonValueReader<>(new ObjectMapper(),
				AccountInfo.class);
	}

	@Test
	public void testGetResult() throws IOException {
		try (InputStream inputStream = getClass().getResourceAsStream("AccountInfo.json")) {
			AccountInfo accountInfo = accountInfoReader.read(inputStream);

			assertFalse(accountInfo.getResult().getBase().isAuthGoogleEnabled());
			assertTrue(accountInfo.getResult().getBase().isAuthMobileEnabled());
			assertTrue(accountInfo.getResult().getBase().isTradePasswordEnabled());
			assertEquals("john", accountInfo.getResult().getBase().getUsername());

			assertEquals(new BigDecimal("0.0001"), accountInfo.getResult().getBalance().get("CNY").getAmount());
			assertEquals("CNY", accountInfo.getResult().getBalance().get("CNY").getCurrency());
			assertEquals("￥", accountInfo.getResult().getBalance().get("CNY").getSymbol());

			assertEquals(new BigDecimal("0.0002"), accountInfo.getResult().getBalance().get("BTC").getAmount());
			assertEquals("BTC", accountInfo.getResult().getBalance().get("BTC").getCurrency());
			assertEquals("฿", accountInfo.getResult().getBalance().get("BTC").getSymbol());

			assertEquals(new BigDecimal("0"), accountInfo.getResult().getBalance().get("LTC").getAmount());
			assertEquals("LTC", accountInfo.getResult().getBalance().get("LTC").getCurrency());
			assertEquals("Ł", accountInfo.getResult().getBalance().get("LTC").getSymbol());

			assertEquals(new BigDecimal("0.0003"), accountInfo.getResult().getFrozen().get("CNY").getAmount());
			assertEquals("CNY", accountInfo.getResult().getFrozen().get("CNY").getCurrency());
			assertEquals("￥", accountInfo.getResult().getFrozen().get("CNY").getSymbol());

			assertEquals(new BigDecimal("0.001"), accountInfo.getResult().getFrozen().get("BTC").getAmount());
			assertEquals("BTC", accountInfo.getResult().getFrozen().get("BTC").getCurrency());
			assertEquals("฿", accountInfo.getResult().getFrozen().get("BTC").getSymbol());

			assertEquals(new BigDecimal("0"), accountInfo.getResult().getFrozen().get("LTC").getAmount());
			assertEquals("LTC", accountInfo.getResult().getFrozen().get("LTC").getCurrency());
			assertEquals("Ł", accountInfo.getResult().getFrozen().get("LTC").getSymbol());

			assertEquals(new BigDecimal("0"), accountInfo.getResult().getP2p().get("inCNY"));
			assertEquals(new BigDecimal("0"), accountInfo.getResult().getP2p().get("inBTC"));
			assertEquals(new BigDecimal("0"), accountInfo.getResult().getP2p().get("inLTC"));
			assertEquals(new BigDecimal("0"), accountInfo.getResult().getP2p().get("outCNY"));
			assertEquals(new BigDecimal("0"), accountInfo.getResult().getP2p().get("outBTC"));
			assertEquals(new BigDecimal("0"), accountInfo.getResult().getP2p().get("outLTC"));
		}
	}

}
