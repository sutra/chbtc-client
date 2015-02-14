package org.oxerr.chbtc.dto;

public enum Type {

	BUY(1, "buy"), SELL(0, "sell");

	public static Type toType(String typeString) {
		for (Type type : Type.values()) {
			if (type.type.equals(typeString)) {
				return type;
			}
		}

		throw new IllegalArgumentException("Unexpected type: " + typeString);
	}

	public static Type toType(int tradeType) {
		for (Type type : Type.values()) {
			if (type.tradeType == tradeType) {
				return type;
			}
		}

		throw new IllegalArgumentException("Unexpected trade type: " + tradeType);
	}

	private int tradeType;

	private String type;

	Type(int tradeType, String type) {
		this.tradeType = tradeType;
		this.type = type;
	}

	public int getTradeType() {
		return tradeType;
	}

}
