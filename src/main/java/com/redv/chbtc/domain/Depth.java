package com.redv.chbtc.domain;

import static java.math.BigDecimal.ONE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj.getClass() != getClass()) {
				return false;
			}
			Data rhs = (Data) obj;
			return new EqualsBuilder()
					.append(getType(), rhs.getType())
					.append(getRate(), rhs.getRate())
					.isEquals();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37)
					.append(getType())
					.append(getRate())
					.toHashCode();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(Data o) {
			BigDecimal direction = getType() == Type.BUY ? ONE.negate() : ONE;
			return new CompareToBuilder()
					.append(getType(), o.getType())
					.append(getRate().multiply(direction),
							o.getRate().multiply(direction))
					.toComparison();
		}

	}

}