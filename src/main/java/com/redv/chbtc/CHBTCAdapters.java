package com.redv.chbtc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.redv.chbtc.domain.Balance;
import com.redv.chbtc.domain.Depth;
import com.redv.chbtc.domain.Order;
import com.redv.chbtc.domain.Result;
import com.redv.chbtc.domain.TickerResponse;
import com.redv.chbtc.domain.Type;
import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.marketdata.Trades.TradeSortType;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.UserTrade;
import com.xeiam.xchange.dto.trade.UserTrades;
import com.xeiam.xchange.dto.trade.Wallet;

/**
 * Various adapters for converting from CHBTC DTOs to XChange DTOs.
 */
public class CHBTCAdapters {

	/**
	 * Private constructor.
	 */
	private CHBTCAdapters() {
	}

	public static Type adaptType(OrderType orderType) {
		return orderType == OrderType.BID ? Type.BUY : Type.SELL;
	}

	public static OrderType adaptOrderType(int type) {
		return adaptOrderType(Type.toType(type));
	}

	public static OrderType adaptOrderType(String type) {
		return adaptOrderType(Type.toType(type));
	}

	public static OrderType adaptOrderType(Type type) {
		return type == Type.BUY ? OrderType.BID : OrderType.ASK;
	}

	public static String adaptCurrency(CurrencyPair currencyPair) {
		return currencyPair.baseSymbol.toLowerCase();
	}

	public static Ticker adaptTicker(TickerResponse tickerResponse,
			CurrencyPair currencyPair) {
		return new Ticker.Builder()
				.currencyPair(currencyPair)
				.high(tickerResponse.getTicker().getHigh())
				.low(tickerResponse.getTicker().getLow())
				.bid(tickerResponse.getTicker().getBuy())
				.ask(tickerResponse.getTicker().getSell())
				.last(tickerResponse.getTicker().getLast())
				.volume(tickerResponse.getTicker().getVol())
				.build();
	}

	public static OrderBook adaptOrderBook(Depth depth, CurrencyPair currencyPair) {
		List<LimitOrder> asks = adaptLimitOrders(OrderType.ASK, depth.getAsks(), currencyPair);

		List<LimitOrder> bids = adaptLimitOrders(OrderType.BID, depth.getBids(), currencyPair);

		return new OrderBook(null, asks, bids);
	}

	public static Trades adaptTrades(com.redv.chbtc.domain.Trade[] trades,
			CurrencyPair currencyPair) {
		List<Trade> tradeList = new ArrayList<>(
				trades.length);
		for (com.redv.chbtc.domain.Trade trade : trades) {
			tradeList.add(adaptTrade(trade, currencyPair));
		}
		long lastTid = trades.length > 0
				? NumberUtils.toLong(trades[trades.length - 1].getTid())
				: 0L;
		return new Trades(tradeList, lastTid, TradeSortType.SortByTimestamp);
	}

	public static AccountInfo adaptAccountInfo(
			com.redv.chbtc.domain.AccountInfo accountInfo) {
		Result result = accountInfo.getResult();
		Map<String, Balance> balance = result.getBalance();
		Map<String, Balance> frozen = result.getFrozen();

		Map<String, BigDecimal> balances = new LinkedHashMap<>(
				Math.max(balance.size(), frozen.size()));

		addBalance(balance, balances);
		addBalance(frozen, balances);

		List<Wallet> wallets = new ArrayList<Wallet>(balances.size());
		for (Map.Entry<String, BigDecimal> entry : balances.entrySet()) {
			wallets.add(new Wallet(entry.getKey(), entry.getValue()));
		}

		return new AccountInfo(result.getBase().getUsername(), wallets);
	}

	public static List<LimitOrder> adaptLimitOrders(Order[] orders) {
		List<LimitOrder> limitOrders = new ArrayList<>(orders.length);
		for (Order order : orders) {
			limitOrders.add(adaptLimitOrder(order));
		}
		return limitOrders;
	}

	public static UserTrades adaptUserTrades(Order order, int priceScale) {
		return adaptUserTrades(new Order[] { order }, priceScale);
	}

	public static UserTrades adaptUserTrades(Order[] orders, int priceScale) {
		List<UserTrade> userTrades = new ArrayList<>(orders.length);
		for (Order order : orders) {
			userTrades.add(adaptUserTrade(order, priceScale));
		}
		return new UserTrades(userTrades, TradeSortType.SortByTimestamp);
	}

	private static List<LimitOrder> adaptLimitOrders(
			OrderType type,
			BigDecimal[][] list,
			CurrencyPair currencyPair) {
		List<LimitOrder> limitOrders = new ArrayList<>(list.length);
		for (BigDecimal[] data : list) {
			limitOrders.add(adaptLimitOrder(type, data, currencyPair));
		}
		return limitOrders;
	}

	private static LimitOrder adaptLimitOrder(
			OrderType type,
			BigDecimal[] data,
			CurrencyPair currencyPair) {
		return new LimitOrder(
				type,
				data[1],
				currencyPair,
				null,
				null,
				data[0]);
	}

	private static LimitOrder adaptLimitOrder(Order order) {
		BigDecimal tradableAmount = order.getTotalAmount().subtract(order.getTradeAmount());
		String currency = order.getCurrency();
		CurrencyPair currencyPair = new CurrencyPair(currency.toUpperCase(), Currencies.CNY);

		return new LimitOrder(
				adaptOrderType(order.getType()),
				tradableAmount,
				currencyPair,
				String.valueOf(order.getId()),
				new Date(order.getTradeDate()),
				order.getPrice());
	}

	private static Trade adaptTrade(
			com.redv.chbtc.domain.Trade trade, CurrencyPair currencyPair) {
		return new Trade(
				adaptOrderType(trade.getType()),
				trade.getAmount(),
				currencyPair,
				trade.getPrice(),
				new Date(Long.parseLong(trade.getDate()) * 1000),
				trade.getTid());
	}

	private static UserTrade adaptUserTrade(Order order, int priceScale) {
		String currency = order.getCurrency();
		CurrencyPair currencyPair = new CurrencyPair(currency.toUpperCase(), Currencies.CNY);
		BigDecimal price = order.getTradeAmount().compareTo(BigDecimal.ZERO) > 0
				? order.getTradeMoney().divide(
						order.getTradeAmount(),
						priceScale,
						RoundingMode.HALF_EVEN)
				: BigDecimal.ZERO;
		return new UserTrade(
				adaptOrderType(order.getType()),
				order.getTradeAmount(),
				currencyPair,
				price,
				new Date(order.getTradeDate()),
				null,
				String.valueOf(order.getId()),
				null,
				null
				);
	}

	private static void addBalance(Map<String, Balance> from,
			Map<String, BigDecimal> to) {
		for (Map.Entry<String, Balance> entry : from.entrySet()) {
			String currency = entry.getKey();
			BigDecimal amount = entry.getValue().getAmount();
			BigDecimal previous = to.get(currency);
			if (previous == null) {
				to.put(currency, amount);
			} else {
				to.put(currency, previous.add(amount));
			}
		}
	}

}
