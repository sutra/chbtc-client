package org.oxerr.chbtc;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.MetaData;

public class CHBTCExchangeTest {

	private final Exchange exchange = ExchangeFactory.INSTANCE.createExchange(CHBTCExchange.class.getName());

	@Test
	public void test() {
		MetaData metaData = exchange.getMetaData();
		List<CurrencyPair> expectedCurrencyPairs = Arrays.asList(CurrencyPair.BTC_CNY, CurrencyPair.LTC_CNY);
		assertEquals(expectedCurrencyPairs, metaData.getCurrencyPairs());
		assertEquals(new Integer(2), metaData.getFiatAmountMultiplier());
		assertEquals(new Integer(3), metaData.getCryptoAmountMultiplier());
	}

}
