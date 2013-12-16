package com.redv.chbtc;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redv.chbtc.domain.Balance;
import com.redv.chbtc.domain.Depth;
import com.redv.chbtc.domain.EntrustDetail;
import com.redv.chbtc.domain.Ticker;
import com.redv.chbtc.domain.Trade;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

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
			log.info("Ticker: {}", ticker);
			log.info("Sell: {}", ticker.getSell());
			log.info("Buy: {}", ticker.getBuy());

			// Depth.
			Depth depth = client.getDepth();
			log.info("Depth.asks: {}", depth.getAsks());
			log.info("Depth.bids: {}", depth.getBids());
			log.info("Lowest ask: {}", depth.getAsks().get(0));
			log.info("Highest bid: {}", depth.getBids().get(0));

			// Trades.
			List<Trade> trades = client.getTrades();
			log.info("Trades: {}", trades);

			trades = client.getTrades(200);
			log.info("Trades since 200: {}", trades);

			// Balance.
			Balance balance;

			try {
				balance = client.getBalance();
			} catch (LoginRequiredException e) {
				client.login();
				balance = client.getBalance();
			}
			log.info("Blance: {}", balance);

			// All entrusts
			List<EntrustDetail> all = client.getAll();
			for (EntrustDetail entrustDetail : all) {
				log.info("{}", entrustDetail);
			}

			for (int page = 1; page <= 2; page++) {
				List<EntrustDetail> allPage = client.getAll(page);
				log.info("get all of page {}, record count {}", page, allPage.size());
				for (EntrustDetail entrustDetail : allPage) {
					log.info("{}", entrustDetail);
				}
			}

			// Buying/Selling entrusts
			client.getBuying();
			List<EntrustDetail> buying = client.getBuying();
			for (EntrustDetail entrustDetail : buying) {
				log.info("{}", entrustDetail);
			}

			for (int page = 1; page <= 2; page++) {
				List<EntrustDetail> buyingPage = client.getBuying(page);
				log.info("get buying of page {}, record count {}", page, buyingPage.size());
				for (EntrustDetail entrustDetail : buyingPage) {
					log.info("{}", entrustDetail);
				}
			}

			List<EntrustDetail> allBuying = client.getAllBuying();
			for (EntrustDetail entrustDetail : allBuying) {
				log.info("{}", entrustDetail);
			}

			// Cancel
			try {
				client.cancel("201312131142385");
			} catch (NoCancelableEntrustException e) {
				log.info("{}", e.getLocalizedMessage());
			}
		}
	}

}
