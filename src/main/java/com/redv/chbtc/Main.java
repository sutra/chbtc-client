package com.redv.chbtc;

import java.io.IOException;
import java.util.List;

import com.redv.chbtc.domain.Balance;
import com.redv.chbtc.domain.Depth;
import com.redv.chbtc.domain.Ticker;
import com.redv.chbtc.domain.Trade;

public class Main {

	public static void main(String[] args) throws IOException {
		try (CHBTCClient client = new CHBTCClient(args[0], args[1])) {
			Ticker ticker = client.getTicker();
			System.out.println(ticker);

			Depth depth = client.getDepth();
			System.out.println("Depth.asks: " + depth.getAsks());
			System.out.println("Depth.bids: " + depth.getBids());

			List<Trade> trades = client.getTrades();
			System.out.println("Trades: " + trades);

			trades = client.getTrades(200);
			System.out.println("Trades since 200: " + trades);

			client.login();

			Balance balance = client.getBalance();
			System.out.println("Blance: " + balance);
		}
	}

}
