package com.redv.chbtc.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 订单。
 */
public class Order extends CHBTCError {

	private static final long serialVersionUID = 2014063001L;

	/**
	 * 挂单 ID。
	 */
	private final long id;

	/**
	 * 挂单类型 1/0[buy/sell]
	 */
	private final int type;

	/**
	 * 单价。
	 */
	private final BigDecimal price;

	/**
	 * 挂单货币类型 btc/ltc。
	 */
	private final String currency;

	/**
	 * 已成交数量。
	 */
	private final BigDecimal tradeAmount;

	/**
	 * 已成交总金额。
	 */
	private final BigDecimal tradeMoney;

	/**
	 * 挂单总数量。
	 */
	private final BigDecimal totalAmount;

	/**
	 * Unix 时间戳。 In milliseconds.
	 */
	private final long tradeDate;

	/**
	 * 挂单状态（0、待成交 1、取消 2、交易完成 3、待成交未成交部分）
	 */
	private final int status;

	public Order(
			@JsonProperty("code") final int code,
			@JsonProperty("message") final String message,
			@JsonProperty("id") final long id,
			@JsonProperty("type") final int type,
			@JsonProperty("price") final BigDecimal price,
			@JsonProperty("currency") final String currency,
			@JsonProperty("trade_amount") final BigDecimal tradeAmount,
			@JsonProperty("trade_money") final BigDecimal tradeMoney,
			@JsonProperty("total_amount") final BigDecimal totalAmount,
			@JsonProperty("trade_date") final long tradeDate,
			@JsonProperty("status") final int status
			) {
		super(code, message);
		this.id = id;
		this.type = type;
		this.price = price;
		this.currency = currency;
		this.tradeAmount = tradeAmount;
		this.tradeMoney = tradeMoney;
		this.totalAmount = totalAmount;
		this.tradeDate = tradeDate;
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getCurrency() {
		return currency;
	}

	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}

	public BigDecimal getTradeMoney() {
		return tradeMoney;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public long getTradeDate() {
		return tradeDate;
	}

	public int getStatus() {
		return status;
	}

}
