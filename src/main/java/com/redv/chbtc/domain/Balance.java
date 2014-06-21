package com.redv.chbtc.domain;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Map;

public class Balance extends AbstractObject {

	private static final long serialVersionUID = 2013113001L;

	/**
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	private BigDecimal rmb;

	/**
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	private BigDecimal rmbFreez;

	/**
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	private BigDecimal btc;

	/**
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	private BigDecimal btcFreez;

	/**
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	private BigDecimal ltc;

	/**
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	private BigDecimal ltcFreez;

	/**
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	private BigDecimal btq;

	/**
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	private BigDecimal btqFreez;

	/**
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	private BigDecimal total;

	/**
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	private String currencyN;

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
	 * @deprecated this constructor is a legacy, from the simulated API wrapper.
	 */
	@Deprecated
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
	 * @deprecated this constructor is a legacy, from the simulated API wrapper.
	 */
	@Deprecated
	public Balance(AccountInfo accountInfo) {
		if (accountInfo.isSuccess()) {
			Result result = accountInfo.getResult();
			Map<String, Balance> balance = result.getBalance();
			Map<String, Balance> frozen = result.getFrozen();
			this.rmb = balance.get("CNY").getAmount();
			this.rmbFreez = frozen.get("CNY").getAmount();
			this.btc = balance.get("BTC").getAmount();
			this.btcFreez = frozen.get("BTC").getAmount();
			this.ltc = balance.get("LTC").getAmount();
			this.ltcFreez = frozen.get("LTC").getAmount();
		}
	}

	/**
	 * @return the rmb
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	public BigDecimal getRmb() {
		return rmb;
	}

	/**
	 * @return the rmbFreez
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	public BigDecimal getRmbFreez() {
		return rmbFreez;
	}

	/**
	 * @return the btc
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	public BigDecimal getBtc() {
		return btc;
	}

	/**
	 * @return the btcFreez
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	public BigDecimal getBtcFreez() {
		return btcFreez;
	}

	/**
	 * @return the ltc
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	public BigDecimal getLtc() {
		return ltc;
	}

	/**
	 * @return the ltcFreez
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	public BigDecimal getLtcFreez() {
		return ltcFreez;
	}

	/**
	 * @return the btq
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	public BigDecimal getBtq() {
		return btq;
	}

	/**
	 * @return the btqFreez
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	public BigDecimal getBtqFreez() {
		return btqFreez;
	}

	/**
	 * @return the total
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @return the currencyN
	 * @deprecated Legacy of simulated API wrapper.
	 */
	@Deprecated
	public String getCurrencyN() {
		return currencyN;
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
