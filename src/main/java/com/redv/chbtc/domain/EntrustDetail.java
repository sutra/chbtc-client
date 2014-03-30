package com.redv.chbtc.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntrustDetail extends AbstractObject {

	public static List<EntrustDetail> toEntrustDetails(List<Order> orders) {
		List<EntrustDetail> entrustDetails = new ArrayList<EntrustDetail>(orders.size());
		for (Order order : orders) {
			entrustDetails.add(new EntrustDetail(order));
		}
		return entrustDetails;
	}

	private static final long serialVersionUID = 2013120901L;

	private String id;

	private Date date;

	private Type type;

	private BigDecimal price;

	private BigDecimal avgPrice;

	private BigDecimal amount;

	private BigDecimal filledAmount;

	private BigDecimal total;

	private BigDecimal filled;

	private Status status;

	public EntrustDetail(
			String id,
			Date date,
			Type type,
			BigDecimal price,
			BigDecimal avgPrice,
			BigDecimal amount,
			BigDecimal filledAmount,
			BigDecimal total,
			BigDecimal filled,
			Status status) {
		this.id = id;
		this.date = date;
		this.type = type;
		this.price = price;
		this.avgPrice = avgPrice;
		this.amount = amount;
		this.filledAmount = filledAmount;
		this.total = total;
		this.filled = filled;
		this.status = status;
	}

	public EntrustDetail(Order order) {
		this.id = String.valueOf(order.getId());
		this.date = order.getTradeDate();
		this.type = order.getType();
		this.price = order.getPrice();
		this.avgPrice = order.getPrice();
		this.amount = order.getTotalAmount();
		this.filledAmount = order.getTradeAmount();
		this.total = order.getPrice().multiply(order.getTotalAmount());
		this.filled = order.getTradeMoney();
		this.status = order.getStatus();
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @return the avgPrice
	 */
	public BigDecimal getAvgPrice() {
		return avgPrice;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @return the filledAmount
	 */
	public BigDecimal getFilledAmount() {
		return filledAmount;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @return the filled
	 */
	public BigDecimal getFilled() {
		return filled;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

}
