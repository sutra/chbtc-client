package com.redv.chbtc.domain;

public enum Status {

	SUCCESS(2), UNFILLED(0), PARTIALLY_FILLED(3), UNKNOWN(-1), CANCEL(1);

	public static Status toStatus(int statusCode) {
		for (Status status : Status.values()) {
			if (status.status == statusCode) {
				return status;
			}
		}

		throw new IllegalArgumentException("Unexpected statuse: " + statusCode);
	}

	private int status;

	Status(int status) {
		this.status = status;
	}

}
