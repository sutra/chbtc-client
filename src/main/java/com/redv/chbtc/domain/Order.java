package com.redv.chbtc.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 订单。
 */
public class Order extends AbstractObject {

	private static final long serialVersionUID = 2014033001L;

	/**
	 * 挂单 ID。
	 */
	private long id;

	/**
	 * 挂单类型 1/0[buy/sell]
	 */
	private int type;

	/**
	 * 单价。
	 */
	private BigDecimal price;

	/**
	 * 挂单货币类型 btc/ltc。
	 */
	private String currency;

	/**
	 * 已成交数量。
	 */
	@JsonProperty("trade_amount")
	private BigDecimal tradeAmount;

	/**
	 * 已成交总金额。
	 */
	@JsonProperty("trade_money")
	private BigDecimal tradeMoney;

	/**
	 * 挂单总数量。
	 */
	@JsonProperty("total_amount")
	private BigDecimal totalAmount;

	/**
	 * Unix 时间戳。
	 */
	@JsonProperty("trade_date")
	private long tradeDate;

	/**
	 * 挂单状态（0、待成交 1、取消 2、交易完成 3、待成交未成交部分）
	 */
	private int status;

	public long getId() {
		return id;
	}

	public Type getType() {
		return Type.toType(type);
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

	public Date getTradeDate() {
		return new Date(tradeDate);
	}

	public Status getStatus() {
		return Status.toStatus(status);
	}

}
