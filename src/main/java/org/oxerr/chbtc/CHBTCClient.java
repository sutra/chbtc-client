package org.oxerr.chbtc;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.message.BasicNameValuePair;
import org.oxerr.chbtc.dto.AccountInfo;
import org.oxerr.chbtc.dto.CHBTCError;
import org.oxerr.chbtc.dto.Depth;
import org.oxerr.chbtc.dto.Order;
import org.oxerr.chbtc.dto.OrderResponse;
import org.oxerr.chbtc.dto.Ticker;
import org.oxerr.chbtc.dto.TickerResponse;
import org.oxerr.chbtc.dto.Trade;
import org.oxerr.chbtc.dto.Type;
import org.oxerr.chbtc.dto.valuereader.DepthReader;
import org.oxerr.chbtc.dto.valuereader.ErrorableJsonValueTypeRefReader;
import org.oxerr.chbtc.dto.valuereader.ValueReader;
import org.oxerr.chbtc.service.polling.CHBTCMarketDataService;
import org.oxerr.chbtc.util.EncryDigestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.currency.CurrencyPair;

public class CHBTCClient implements AutoCloseable {

	public static final String ENCODING = "UTF-8";

	@Deprecated
	private static final URI API_BASE_URI = URI.create("http://api.chbtc.com/");

	@Deprecated
	private static final URI DATA_BASE_URI = URIUtils.resolve(API_BASE_URI, "data/");

	@Deprecated
	private static final URI TICKER_URI = URIUtils.resolve(DATA_BASE_URI, "ticker");

	@Deprecated
	private static final URI DEPTH_URI = URIUtils.resolve(DATA_BASE_URI, "depth");

	@Deprecated
	private static final URI TRADES_URI = URIUtils.resolve(DATA_BASE_URI, "trades");

	@Deprecated
	private static final URI LTC_DATA_BASE_URI = URIUtils.resolve(DATA_BASE_URI, "ltc");

	@Deprecated
	private static final URI LTC_TICKER_URI = URIUtils.resolve(LTC_DATA_BASE_URI, "ticker");

	@Deprecated
	private static final URI LTC_DEPTH_URI = URIUtils.resolve(LTC_DATA_BASE_URI, "depth");

	@Deprecated
	private static final URI LTC_TRADES_URI = URIUtils.resolve(LTC_DATA_BASE_URI, "trades");

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Deprecated
	private static final DepthReader DEPTH_READER = new DepthReader(OBJECT_MAPPER);

	@Deprecated
	private static final TypeReference<List<Trade>> TRADE_LIST_TYPE_REFERENCE = new TypeReference<List<Trade>>() {
	};

	private static final TypeReference<List<Order>> ORDER_LIST_TYPE_REFERENCE = new TypeReference<List<Order>>() {
	};

	private static final ValueReader<List<Order>> ORDER_LIST_READER = new ErrorableJsonValueTypeRefReader<>(OBJECT_MAPPER, ORDER_LIST_TYPE_REFERENCE);

	private final Logger log = LoggerFactory.getLogger(CHBTCClient.class);

	private final Map<String, Long> lastReqTime = new HashMap<>(8);

	private final HttpClient httpClient;

	private final String tradeApiUrl;

	private final String accessKey;

	private final String secret;

	public CHBTCClient(
			final String accessKey,
			final String secretKey,
			final int socketTimeout,
			final int connectTimeout,
			final int connectionRequestTimeout) {
		this("https://trade.chbtc.com/api", accessKey, secretKey,
				socketTimeout, connectTimeout, connectionRequestTimeout);
	}

	public CHBTCClient(
			final String tradeApiUrl,
			final String accessKey,
			final String secretKey,
			final int socketTimeout,
			final int connectTimeout,
			final int connectionRequestTimeout) {
		this.tradeApiUrl = tradeApiUrl;

		httpClient = new HttpClient(
				socketTimeout,
				connectTimeout,
				connectionRequestTimeout);

		this.accessKey = accessKey;
		this.secret = EncryDigestUtil.digest(secretKey);
	}

	/**
	 * Gets BTC ticker.
	 *
	 * @return the ticker of the BTC market.
	 * @throws IOException indicates I/O exception.
	 * @deprecated Use {@link CHBTCMarketDataService#getTicker(CurrencyPair)} instead.
	 */
	@Deprecated
	public Ticker getTicker() throws IOException {
		return httpClient.get(TICKER_URI, TickerResponse.class).getTicker();
	}

	/**
	 * Gets ticker of the specified market.
	 * 
	 * @param currency the currency symbol of the tradable, could be BTC or LTC.
	 * @return the ticker.
	 * @throws IOException indicates I/O exception.
	 * @deprecated Use {@link CHBTCMarketDataService#getTicker(CurrencyPair)} instead.
	 */
	@Deprecated
	public Ticker getTicker(String currency) throws IOException {
		if (currency.equalsIgnoreCase(Currencies.LTC)) {
			return httpClient.get(LTC_TICKER_URI, TickerResponse.class)
					.getTicker();
		} else {
			return getTicker();
		}
	}

	/**
	 * Gets BTC market order book.
	 *
	 * @return the order book of BTC market.
	 * @throws IOException indicates I/O exception.
	 * @deprecated Use {@link CHBTCMarketDataService#getOrderBook(CurrencyPair, Object...)} instead.
	 */
	@Deprecated
	public Depth getDepth() throws IOException {
		return httpClient.get(DEPTH_URI, DEPTH_READER);
	}

	/**
	 * Gets order book of the specified market.
	 *
	 * @param currency the currency symbol of the tradable, could be BTC or LTC.
	 * @return the order book of the specified market.
	 * @throws IOException indicates I/O exception.
	 * @deprecated Use {@link CHBTCMarketDataService#getOrderBook(CurrencyPair, Object...)} instead.
	 */
	@Deprecated
	public Depth getDepth(String currency) throws IOException {
		if (currency.equalsIgnoreCase(Currencies.LTC)) {
			return httpClient.get(LTC_DEPTH_URI, DEPTH_READER);
		} else {
			return getDepth();
		}
	}

	/**
	 * Gets trades of BTC market.
	 *
	 * @return the trades of BTC market.
	 * @throws IOException indicates I/O exception.
	 * @deprecated Use {@link CHBTCMarketDataService#getTrades(CurrencyPair, Object...)} instead.
	 */
	@Deprecated
	public List<Trade> getTrades() throws IOException {
		return httpClient.get(TRADES_URI, TRADE_LIST_TYPE_REFERENCE);
	}

	/**
	 * Gets trades of BTC market.
	 *
	 * @param since 1 based. When pass 1, will get trades from the first trade of CHBTC.
	 * @return the trades in BTC market.
	 * @throws IOException indicates I/O exception.
	 * @deprecated Use {@link CHBTCMarketDataService#getTrades(CurrencyPair, Object...)} instead.
	 */
	@Deprecated
	public List<Trade> getTrades(int since) throws IOException {
		final URI uri;
		try {
			uri = new URIBuilder(TRADES_URI)
				.setParameter("since", String.valueOf(since))
				.build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}

		return httpClient.get(uri, TRADE_LIST_TYPE_REFERENCE);
	}

	/**
	 * Gets recent trades of the specified market.
	 *
	 * @param currency the currency symbol of the tradable, could be BTC or LTC.
	 * @return the trades.
	 * @throws IOException indicates I/O exception.
	 * @deprecated Use {@link CHBTCMarketDataService#getTrades(CurrencyPair, Object...)} instead.
	 */
	@Deprecated
	public List<Trade> getTrades(String currency) throws IOException {
		if (currency.equalsIgnoreCase(Currencies.LTC)) {
			return httpClient.get(LTC_TRADES_URI, TRADE_LIST_TYPE_REFERENCE);
		} else {
			return getTrades();
		}
	}

	/**
	 * Gets trades.
	 *
	 * @param currency the currency symbol of the tradable, could be BTC or LTC.
	 * @param since 1 based. When pass 1, will get trades from the first trade of CHBTC.
	 * @return trades.
	 * @throws IOException indicates I/O exception.
	 * @deprecated Use {@link CHBTCMarketDataService#getTrades(CurrencyPair, Object...)} instead.
	 */
	@Deprecated
	public List<Trade> getTrades(
			String currency,
			int since
			) throws IOException {
		if (currency.equalsIgnoreCase(Currencies.LTC)) {
			final URI uri;
			try {
				uri = new URIBuilder(LTC_TRADES_URI)
					.setParameter("since", String.valueOf(since))
					.build();
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException(e);
			}

			return httpClient.get(uri, TRADE_LIST_TYPE_REFERENCE);
		} else {
			return getTrades(since);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		log.debug("Closing HTTP Client...");
		httpClient.close();
	}

	/**
	 * 委托 BTC/LTC 买单或卖单。
	 *
	 * @param price 单价。
	 * @param amount 交易数量。
	 * @param tradeType 交易类型 1/0[buy/sell]
	 * @param currency 交易类型(目前仅支持 BTC/LTC)。
	 * @return 挂单 ID。
	 * @throws IOException indicates I/O exception.
	 */
	public long order(
			final BigDecimal price,
			final BigDecimal amount,
			final Type tradeType,
			final String currency
			) throws IOException {
		OrderResponse orderResponse = get(
				CHBTC.METHOD_ORDER,
				OrderResponse.class,
				new BasicNameValuePair("price", price.toPlainString()),
				new BasicNameValuePair("amount", amount.toPlainString()),
				new BasicNameValuePair("tradeType",
						String.valueOf(tradeType.getTradeType())),
				new BasicNameValuePair("currency", currency));
		if (orderResponse.getCode() == CHBTCError.SUCCESS) {
			return orderResponse.getId();
		} else {
			throw new CHBTCClientException(orderResponse);
		}
	}

	/**
	 * 取消委托买单或卖单。
	 *
	 * @param id 挂单 ID。
	 * @param currency 交易类型(目前仅支持 BTC/LTC)。
	 * @throws IOException indicates I/O exception.
	 */
	public void cancelOrder(
			final long id,
			final String currency
			) throws IOException {
		CHBTCError error = get(
				CHBTC.METHOD_CANCEL_ORDER,
				CHBTCError.class,
				new BasicNameValuePair("id", String.valueOf(id)),
				new BasicNameValuePair("currency", currency));

		if (error.getCode() != CHBTCError.SUCCESS) {
			throw new CHBTCClientException(error);
		}
	}

	/**
	 * 获取委托买单或卖单。
	 *
	 * @param id 挂单 ID。
	 * @param currency 交易类型(目前仅支持 BTC/LTC)。
	 * @return 委托买单或卖单。
	 * @throws IOException indicates I/O exception.
	 */
	public Order getOrder(
			final long id,
			final String currency
			) throws IOException {
		return get(
				CHBTC.METHOD_GET_ORDER,
				Order.class,
				new BasicNameValuePair("id", String.valueOf(id)),
				new BasicNameValuePair("currency", currency));
	}

	/**
	 * 获取多个委托买单或卖单,每次请求返回 10 条记录。
	 *
	 * @param tradeType 交易类型 1/0[buy/sell]。
	 * @param currency 交易类型(目前仅支持 BTC/LTC)。
	 * @param pageIndex 当前页数。
	 * @return 多个委托买单或卖单，最多 10 条记录。
	 * @throws IOException indicates I/O exception.
	 */
	public List<Order> getOrders(
			final Type tradeType,
			final String currency,
			final int pageIndex
			) throws IOException {
		return get(
				CHBTC.METHOD_GET_ORDERS,
				ORDER_LIST_READER,
				new BasicNameValuePair("tradeType",
						String.valueOf(tradeType.getTradeType())),
				new BasicNameValuePair("currency", currency),
				new BasicNameValuePair("pageIndex", String.valueOf(pageIndex)));
	}

	/**
	 * 获取多个委托买单或卖单，每次请求返回 {@literal pageSize<=100} 条记录。
	 *
	 * @param tradeType 交易类型 1/0[buy/sell]。
	 * @param currency 交易类型(目前仅支持 BTC/LTC)。
	 * @param pageIndex 当前页数。
	 * @param pageSize 每页数量。
	 * @return 多个委托买单或卖单。
	 * @throws IOException indicates I/O exception.
	 */
	public List<Order> getOrdersNew(
			final Type tradeType,
			final String currency,
			final int pageIndex,
			final int pageSize
			) throws IOException {
		return get(
				CHBTC.METHOD_GET_ORDERS_NEW,
				ORDER_LIST_READER,
				new BasicNameValuePair("tradeType",
						String.valueOf(tradeType.getTradeType())),
				new BasicNameValuePair("currency", currency),
				new BasicNameValuePair("pageIndex", String.valueOf(pageIndex)),
				new BasicNameValuePair("pageSize", String.valueOf(pageSize)));
	}

	/**
	 * 与 gerOrders 的区别是取消 tradeType 字段过滤，可同时获取买单和卖单。
	 *
	 * @param currency 交易类型(目前仅支持 BTC/LTC)。
	 * @param pageIndex 当前页数。
	 * @param pageSize 每页数量。
	 * @return 多个委托买单和卖单。
	 * @throws IOException indicates I/O exception.
	 */
	public List<Order> getOrdersIgnoreTradeType(
			final String currency,
			final int pageIndex,
			final int pageSize
			) throws IOException {
		return get(
				CHBTC.METHOD_GET_ORDERS_IGNORE_TRADE_TYPE,
				ORDER_LIST_READER,
				new BasicNameValuePair("currency", currency),
				new BasicNameValuePair("pageIndex", String.valueOf(pageIndex)),
				new BasicNameValuePair("pageSize", String.valueOf(pageSize)));
	}

	/**
	 * 获取未成交或部份成交的买单和卖单，每次请求返回 {@literal pageSize<=100} 条记录。
	 *
	 * @param currency 交易类型(目前仅支持 BTC/LTC)。
	 * @param pageIndex 当前页数。
	 * @param pageSize 每页数量。
	 * @return 未成交或部份成交的买单和卖单。
	 * @throws IOException indicates I/O exception.
	 */
	public List<Order> getUnfinishedOrdersIgnoreTradeType(
			final String currency,
			final int pageIndex,
			final int pageSize
			) throws IOException {
		try {
			return getUnfinishedOrdersIgnoreTradeTypeInternal(currency,
					pageIndex, pageSize);
		} catch (CHBTCClientException e) {
			if (e.getErrorCode() == CHBTCError.NOT_FOUND_ORDER) {
				return Collections.emptyList();
			} else {
				throw e;
			}
		}
	}

	/**
	 * 获取用户信息。
	 * @return 用户信息。
	 * @throws IOException indicates I/O exception.
	 */
	public AccountInfo getAccountInfo() throws IOException {
		return get(CHBTC.METHOD_GET_ACCOUNT_INFO, AccountInfo.class);
	}

	private List<Order> getUnfinishedOrdersIgnoreTradeTypeInternal(
			final String currency, final int pageIndex, final int pageSize)
			throws IOException {
		return get(
				CHBTC.METHOD_GET_UNFINISHED_ORDERS_IGNORE_TRADE_TYPE,
				ORDER_LIST_READER,
				new BasicNameValuePair("currency", currency),
				new BasicNameValuePair("pageIndex", String.valueOf(pageIndex)),
				new BasicNameValuePair("pageSize", String.valueOf(pageSize)));
	}

	private <T> T get(
			String method,
			Class<T> valueType,
			NameValuePair... params
			) throws IOException {
		sleepIfRequired(method);

		URI uri = buildTradeUri(method, params);
		T t = httpClient.get(uri, valueType);

		this.lastReqTime.put(method, System.currentTimeMillis());

		return t;
	}

	private <T> T get(
			String method,
			ValueReader<T> valueReader,
			NameValuePair... params
			) throws IOException {
		sleepIfRequired(method);

		URI uri = buildTradeUri(method, params);
		T t = httpClient.get(uri, valueReader);

		this.lastReqTime.put(method, System.currentTimeMillis());

		return t;
	}

	private URI buildTradeUri(String method, NameValuePair... nameValuePairs) {
		final StringBuilder paramsBuilder = new StringBuilder("method=")
			.append(method)
			.append("&accesskey=").append(accessKey);
		for (NameValuePair pair : nameValuePairs) {
			paramsBuilder.append("&")
				.append(pair.getName()).append("=").append(pair.getValue());
		}

		final String params = paramsBuilder.toString();

		final String sign = EncryDigestUtil.hmacSign(params, secret);

		final String uri = new StringBuilder(tradeApiUrl)
			.append("/")
			.append(method)
			.append("?")
			.append(params)
			.append("&sign=").append(sign)
			.append("&reqTime=").append(System.currentTimeMillis())
			.toString();

		return URI.create(uri);
	}

	/**
	 * 因为请求同一个方法，1 秒内只能请求一次，所以必须控制请求频率。
	 */
	private void sleepIfRequired(String method) {
		Long last = lastReqTime.get(method);
		long now = System.currentTimeMillis();
		if (last != null && now - last.longValue() < 1000) {
			sleepQuietly(1000 - (now - last.longValue()));
		}
	}

	private void sleepQuietly(long millis) {
		try {
			log.debug("Sleeping {} ms.", millis);
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// Ignore.
		}
	}

}
