package org.oxerr.chbtc.dto;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Balance extends BaseObject {

	private static final long serialVersionUID = 2014063001L;

	/**
	 * 货币标识。
	 */
	private final String currency;

	/**
	 * 货币符号。
	 */
	private final String symbol;

	/**
	 * 货币总量。
	 */
	private final BigDecimal amount;

	/**
	 * Constructor.
	 *
	 * @param currency the currency symbol.
	 * @param symbol the URL encoded currency symbol.
	 * @param amount the amount of the currency.
	 */
	public Balance(
			@JsonProperty("currency") final String currency,
			@JsonProperty("symbol") final String symbol,
			@JsonProperty("amount") final BigDecimal amount) {
		this.currency = currency;
		try {
			this.symbol = URLDecoder.decode(symbol, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		this.amount = amount;
	}

	/**
	 * The currency code, e.g. CNY, BTC, LTC.
	 * 
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Returns currency symbol, e.g. ￥ for CNY, ฿ for BTC, Ł for LTC.
	 * 
	 * @return the currency symbol.
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * Balance amount.
	 * 
	 * @return The balance amount.
	 */
	public BigDecimal getAmount() {
		return amount;
	}

}
