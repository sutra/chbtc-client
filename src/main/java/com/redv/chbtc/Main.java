package com.redv.chbtc;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redv.chbtc.domain.Balance;
import com.redv.chbtc.domain.EntrustDetail;
import com.redv.chbtc.domain.Order;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.service.polling.PollingAccountService;
import com.xeiam.xchange.service.polling.PollingMarketDataService;
import com.xeiam.xchange.service.polling.PollingTradeService;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws IOException {
		final String accessKey = args[0];
		final String secretKey = args[1];

		// Market data service
		Exchange publicExchange = ExchangeFactory.INSTANCE.createExchange(CHBTCExchange.class.getName());
		PollingMarketDataService marketDataService = publicExchange.getPollingMarketDataService();

		// Ticker
		com.xeiam.xchange.dto.marketdata.Ticker ticker0 = marketDataService.getTicker(CurrencyPair.BTC_CNY);
		log.info("BTC ticker: {}", ticker0);
		ticker0 = marketDataService.getTicker(CurrencyPair.LTC_CNY);
		log.info("LTC ticker: {}", ticker0);

		// Depth
		OrderBook orderBook = marketDataService.getOrderBook(CurrencyPair.BTC_CNY);
		log.info("BTC order book: {}", orderBook);
		orderBook = marketDataService.getOrderBook(CurrencyPair.LTC_CNY);
		log.info("LTC order book: {}", orderBook);

		// Trades
		Trades trades0 = marketDataService.getTrades(CurrencyPair.BTC_CNY);
		log.info("BTC trades: {}", trades0);
		trades0 = marketDataService.getTrades(CurrencyPair.BTC_CNY, trades0.getlastID());
		log.info("BTC trades since {}: {}", trades0.getlastID(), trades0);
		trades0 = marketDataService.getTrades(CurrencyPair.LTC_CNY);
		log.info("LTC trades: {}", trades0);
		trades0 = marketDataService.getTrades(CurrencyPair.LTC_CNY, trades0.getlastID());
		log.info("LTC trades since {}: {}", trades0.getlastID(), trades0);

		ExchangeSpecification spec = new ExchangeSpecification(CHBTCExchange.class);
		spec.setApiKey(accessKey);
		spec.setSecretKey(secretKey);

		// Account service.
		Exchange tradeExchange = ExchangeFactory.INSTANCE.createExchange(spec);
		PollingAccountService accountService = tradeExchange.getPollingAccountService();

		AccountInfo accountInfo = accountService.getAccountInfo();
		log.info("Account info: {}", accountInfo);

		PollingTradeService tradeSerivce = tradeExchange.getPollingTradeService();

		// Open orders
		OpenOrders openOrders = tradeSerivce.getOpenOrders();
		log.info("Open orders({}): {}", openOrders.getOpenOrders().size(), openOrders);

		// Trade history
		Trades tradeHistory = tradeSerivce.getTradeHistory(CurrencyPair.BTC_CNY, 0, 1);
		log.info("Trade history({}): {}", tradeHistory.getTrades().size(), tradeHistory);

		try (CHBTCClient client = new CHBTCClient(accessKey, secretKey, 5000, 5000, 5000)) {
			// getUnfinishedOrdersIgnoreTradeType
			List<Order> orders = client.getUnfinishedOrdersIgnoreTradeType("BTC", 1, 20);
			for (Order order : orders) {
				System.out.println(order);
			}

			// Balance.
			Balance balance = client.getBalance();
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
