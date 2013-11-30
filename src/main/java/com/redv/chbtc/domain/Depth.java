package com.redv.chbtc.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Depth extends AbstractObject {

	private static final long serialVersionUID = 2013112501L;

	private List<Data> asks;

	private List<Data> bids;

	public List<Data> getAsks() {
		return asks;
	}

	public List<Data> getBids() {
		return bids;
	}

	public void setAsks(final BigDecimal[][] asks) {
		this.asks = new ArrayList<>(asks.length);
		for (final BigDecimal[] ask : asks) {
			this.asks.add(new Data(Type.SELL, ask[0], ask[1]));
		}
		Collections.sort(this.asks);
	}

	public void setBids(final BigDecimal[][] bids) {
		this.bids = new ArrayList<>(bids.length);
		for (final BigDecimal[] bid : bids) {
			this.bids.add(new Data(Type.BUY, bid[0], bid[1]));
		}
		Collections.sort(this.bids);
	}

	public static class Data extends AbstractObject implements Comparable<Data> {

		private static final long serialVersionUID = 2013112501L;

		private final Type type;

		private final BigDecimal rate;

		private final BigDecimal amount;

		public Data(Type type, BigDecimal rate, BigDecimal amount) {
			this.type = type;
			this.rate = rate;
			this.amount = amount;
		}

		/**
		 * @return the type
		 */
		public Type getType() {
			return type;
		}

		/**
		 * @return the rate
		 */
		public BigDecimal getRate() {
			return rate;
		}

		/**
		 * @return the amount
		 */
		public BigDecimal getAmount() {
			return amount;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(Data o) {
			return getRate().compareTo(o.getRate()) * (getType() == Type.BUY ? -1 : 1);
		}

	}

}