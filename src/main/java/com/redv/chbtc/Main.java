package com.redv.chbtc;

import java.io.IOException;
import java.util.List;

import com.redv.chbtc.domain.Balance;
import com.redv.chbtc.domain.Depth;
import com.redv.chbtc.domain.EntrustDetail;
import com.redv.chbtc.domain.Ticker;
import com.redv.chbtc.domain.Trade;

public class Main {

	public static void main(String[] args) throws IOException {
		try (CHBTCClient client = new CHBTCClient(args[0], args[1])) {
			// Ticker.
			Ticker ticker = client.getTicker();
			System.out.println("Ticker: " + ticker);
			System.out.println("Sell: " + ticker.getSell());
			System.out.println("Buy: " + ticker.getBuy());

			// Depth.
			Depth depth = client.getDepth();
			System.out.println("Depth.asks: " + depth.getAsks());
			System.out.println("Depth.bids: " + depth.getBids());
			System.out.println("Lowest ask: " + depth.getAsks().get(0));
			System.out.println("Highest bid: " + depth.getBids().get(0));

			// Trades.
			List<Trade> trades = client.getTrades();
			System.out.println("Trades: " + trades);

			trades = client.getTrades(200);
			System.out.println("Trades since 200: " + trades);

			// Balance.
			Balance balance;

			try {
				balance = client.getBalance();
			} catch (LoginRequiredException e) {
				client.login();
				balance = client.getBalance();
			}
			System.out.println("Blance: " + balance);

			// All entrusts
			List<EntrustDetail> all = client.getAll();
			for (EntrustDetail entrustDetail : all) {
				System.out.println(entrustDetail);
			}

			// Buying/Selling entrusts
			client.getBuying();
			List<EntrustDetail> buying = client.getBuying();
			for (EntrustDetail entrustDetail : buying) {
				System.out.println(entrustDetail);
			}
		}
	}

}
