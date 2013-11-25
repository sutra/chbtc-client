package com.redv.chbtc;

import java.io.IOException;
import java.util.List;

import com.redv.chbtc.domain.Depth;
import com.redv.chbtc.domain.Ticker;
import com.redv.chbtc.domain.Trade;

public class Main {

	public static void main(String[] args) throws IOException {
		try (CHBTCClient client = new CHBTCClient()) {
			Ticker ticker = client.getTicker();
			System.out.println(ticker);

			Depth depth = client.getDepth();
			System.out.println(depth.getAsks());
			System.out.println(depth.getBids());

			List<Trade> trades = client.getTrades();
			System.out.println(trades);

			trades = client.getTrades(200);
			System.out.println(trades);
		}
	}

}
