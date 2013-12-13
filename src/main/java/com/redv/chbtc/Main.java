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
		final String username = args[0];
		final String password = args[1];

		try (CHBTCClient client = new CHBTCClient(username, password)) {
			// do nothing, just to test the logout calling when does not login
			// in close method.
		}

		try (CHBTCClient client = new CHBTCClient(username, password)) {
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

			for (int page = 1; page <= 2; page++) {
				List<EntrustDetail> allPage = client.getAll(page);
				System.out.println("get all of page " + page + ", record count " + allPage.size());
				for (EntrustDetail entrustDetail : allPage) {
					System.out.println(entrustDetail);
				}
			}

			// Buying/Selling entrusts
			client.getBuying();
			List<EntrustDetail> buying = client.getBuying();
			for (EntrustDetail entrustDetail : buying) {
				System.out.println(entrustDetail);
			}

			for (int page = 1; page <= 2; page++) {
				List<EntrustDetail> buyingPage = client.getBuying(page);
				System.out.println("get buying of page " + page + ", record count " + buyingPage.size());
				for (EntrustDetail entrustDetail : buyingPage) {
					System.out.println(entrustDetail);
				}
			}

			List<EntrustDetail> allBuying = client.getAllBuying();
			for (EntrustDetail entrustDetail : allBuying) {
				System.out.println(entrustDetail);
			}

			// Cancel
			try {
				client.cancel("201312131142385");
			} catch (NoCancelableEntrustException e) {
				System.out.println(e.getLocalizedMessage());
			}
		}
	}

}
