package com.redv.chbtc.domain;

import java.math.BigDecimal;

public class Balance extends AbstractObject {

	private static final long serialVersionUID = 2013113001L;

	private BigDecimal rmb;

	private BigDecimal rmbFreez;

	private BigDecimal btc;

	private BigDecimal btcFreez;

	private BigDecimal ltc;

	private BigDecimal ltcFreez;

	private BigDecimal btq;

	private BigDecimal btqFreez;

	private BigDecimal total;

	private String currencyN;

	private String currency;

	public Balance() {
	}

	public Balance(BigDecimal rmb, BigDecimal rmbFreez,
			BigDecimal btc, BigDecimal btcFreez,
			BigDecimal ltc, BigDecimal ltcFreez,
			BigDecimal btq, BigDecimal btqFreez,
			BigDecimal total, String currencyN, String currency) {
		this.rmb = rmb;
		this.rmbFreez = rmbFreez;
		this.btc = btc;
		this.btcFreez = btcFreez;
		this.ltc = ltc;
		this.ltcFreez = ltcFreez;
		this.btq = btq;
		this.btqFreez = btqFreez;
		this.total = total;
		this.currencyN = currencyN;
		this.currency = currency;
	}

	/**
	 * @return the rmb
	 */
	public BigDecimal getRmb() {
		return rmb;
	}

	/**
	 * @return the rmbFreez
	 */
	public BigDecimal getRmbFreez() {
		return rmbFreez;
	}

	/**
	 * @return the btc
	 */
	public BigDecimal getBtc() {
		return btc;
	}

	/**
	 * @return the btcFreez
	 */
	public BigDecimal getBtcFreez() {
		return btcFreez;
	}

	/**
	 * @return the ltc
	 */
	public BigDecimal getLtc() {
		return ltc;
	}

	/**
	 * @return the ltcFreez
	 */
	public BigDecimal getLtcFreez() {
		return ltcFreez;
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
