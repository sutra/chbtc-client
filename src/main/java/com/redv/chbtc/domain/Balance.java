package com.redv.chbtc.domain;

import java.math.BigDecimal;

public class Balance extends AbstractObject {

	private static final long serialVersionUID = 2013113001L;

	private BigDecimal rmb;

	private BigDecimal btc;

	private BigDecimal rmbFreez;

	private BigDecimal btq;

	private BigDecimal btqFreez;

	private BigDecimal btcFreez;

	private BigDecimal total;

	private String currencyN;

	private String currency;

	/**
	 * @return the rmb
	 */
	public BigDecimal getRmb() {
		return rmb;
	}

	/**
	 * @return the btc
	 */
	public BigDecimal getBtc() {
		return btc;
	}

	/**
	 * @return the rmbFreez
	 */
	public BigDecimal getRmbFreez() {
		return rmbFreez;
	}

	/**
	 * @return the btq
	 */
	public BigDecimal getBtq() {
		return btq;
	}

	/**
	 * @return the btqFreez
	 */
	public BigDecimal getBtqFreez() {
		return btqFreez;
	}

	/**
	 * @return the btcFreez
	 */
	public BigDecimal getBtcFreez() {
		return btcFreez;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @return the currencyN
	 */
	public String getCurrencyN() {
		return currencyN;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

}
