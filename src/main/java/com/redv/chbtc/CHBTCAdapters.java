package com.redv.chbtc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.redv.chbtc.domain.Depth;
import com.redv.chbtc.domain.Depth.Data;
import com.redv.chbtc.domain.TickerResponse;
import com.redv.chbtc.domain.Trade;
import com.redv.chbtc.domain.Type;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.marketdata.Ticker.TickerBuilder;
import com.xeiam.xchange.dto.marketdata.Trades.TradeSortType;
import com.xeiam.xchange.dto.trade.LimitOrder;

/**
 * Various adapters fro converting from CHBTC DTOs to XChange DTOs.
 */
public class CHBTCAdapters {

	/**
	 * Private constructor.
	 */
	private CHBTCAdapters() {
	}

	public static Ticker adaptTicker(TickerResponse tickerResponse,
			CurrencyPair currencyPair) {
		return TickerBuilder
				.newInstance()
				.withCurrencyPair(currencyPair)
				.withHigh(tickerResponse.getTicker().getHigh())
				.withLow(tickerResponse.getTicker().getLow())
				.withBid(tickerResponse.getTicker().getBuy())
				.withAsk(tickerResponse.getTicker().getSell())
				.withLast(tickerResponse.getTicker().getLast())
				.withVolume(tickerResponse.getTicker().getVol())
				.build();
	}

	public static OrderBook adaptOrderBook(Depth depth, CurrencyPair currencyPair) {
		return new OrderBook(
				null,
				adaptLimitOrders(depth.getAsks(), currencyPair),
				adaptLimitOrders(depth.getBids(), currencyPair));
	}

	public static Trades adaptTrades(Trade[] trades, CurrencyPair currencyPair) {
		List<com.xeiam.xchange.dto.marketdata.Trade> tradeList = new ArrayList<>(
				trades.length);
		for (Trade trade : trades) {
			tradeList.add(adaptTrade(trade, currencyPair));
		}
		long lastTid = trades.length > 0
				? NumberUtils.toLong(trades[trades.length - 1].getTid())
				: 0L;
		return new Trades(tradeList, lastTid, TradeSortType.SortByTimestamp);
	}

	private static List<LimitOrder> adaptLimitOrders(List<Data> list,
			CurrencyPair currencyPair) {
		List<LimitOrder> limitOrders = new ArrayList<>(list.size());
		for (Data data : list) {
			limitOrders.add(adaptLimitOrder(data, currencyPair));
		}
		return limitOrders;
	}

	private static LimitOrder adaptLimitOrder(Data data,
			CurrencyPair currencyPair) {
		return new LimitOrder(adaptOrderType(data.getType()),
				data.getAmount(),
				currencyPair,
				null,
				null,
				data.getRate());
	}

	private static OrderType adaptOrderType(Type type) {
		return type == Type.BUY ? OrderType.BID : OrderType.ASK;
	}

	private static com.xeiam.xchange.dto.marketdata.Trade adaptTrade(
			Trade trade, CurrencyPair currencyPair) {
		return new com.xeiam.xchange.dto.marketdata.Trade(
				adaptOrderType(trade.getType()),
				trade.getAmount(),
				currencyPair,
				trade.getPrice(),
				trade.getDate(),
				trade.getTid());
	}

}
