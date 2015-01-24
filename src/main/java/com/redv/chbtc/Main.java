package com.redv.chbtc;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redv.chbtc.domain.Order;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.service.polling.account.PollingAccountService;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;
import com.xeiam.xchange.service.polling.trade.PollingTradeService;

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
		Trades tradeHistory = tradeSerivce.getTradeHistory(CurrencyPair.BTC_CNY, null, 1, 1);
		log.info("Trade history({}): {}", tradeHistory.getTrades().size(), tradeHistory);

		try (CHBTCClient client = new CHBTCClient(accessKey, secretKey, 5000, 5000, 5000)) {
			// getUnfinishedOrdersIgnoreTradeType
			List<Order> orders = client.getUnfinishedOrdersIgnoreTradeType("BTC", 1, 20);
			for (Order order : orders) {
				System.out.println(order);
			}
		}
	}

}
