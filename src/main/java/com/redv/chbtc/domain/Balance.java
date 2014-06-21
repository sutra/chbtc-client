package com.redv.chbtc.domain;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;

public class Balance extends AbstractObject {

	private static final long serialVersionUID = 2014062201L;

	/**
	 * 货币标识。
	 */
	private String currency;

	/**
	 * 货币符号。
	 */
	private String symbol;

	/**
	 * 货币总量。
	 */
	private BigDecimal amount;

	public Balance() {
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
	 * Sets the currency symbol.
	 * 
	 * @param symbol the URL encoded symbol.
	 */
	public void setSymbol(String symbol) {
		try {
			this.symbol = URLDecoder.decode(symbol, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
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
