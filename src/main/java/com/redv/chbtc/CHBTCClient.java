package com.redv.chbtc;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redv.chbtc.domain.AccountInfo;
import com.redv.chbtc.domain.Balance;
import com.redv.chbtc.domain.CHBTCError;
import com.redv.chbtc.domain.Depth;
import com.redv.chbtc.domain.EntrustDetail;
import com.redv.chbtc.domain.Order;
import com.redv.chbtc.domain.OrderResponse;
import com.redv.chbtc.domain.Ticker;
import com.redv.chbtc.domain.TickerResponse;
import com.redv.chbtc.domain.Trade;
import com.redv.chbtc.domain.Type;
import com.redv.chbtc.util.EncryDigestUtil;
import com.redv.chbtc.valuereader.DepthReader;
import com.redv.chbtc.valuereader.ErrorableJsonValueTypeRefReader;
import com.redv.chbtc.valuereader.ValueReader;

public class CHBTCClient implements AutoCloseable {

	public static final String ENCODING = "UTF-8";

	private static final String CURRENCY_BTC = "BTC";
	private static final String CURRENCY_LTC = "LTC";

	private static final URI API_BASE_URI = URI.create("http://api.chbtc.com/");

	private static final URI DATA_BASE_URI = URIUtils.resolve(API_BASE_URI, "data/");

	private static final URI TICKER_URI = URIUtils.resolve(DATA_BASE_URI, "ticker");
	private static final URI DEPTH_URI = URIUtils.resolve(DATA_BASE_URI, "depth");
	private static final URI TRADES_URI = URIUtils.resolve(DATA_BASE_URI, "trades");

	private static final URI LTC_DATA_BASE_URI = URIUtils.resolve(DATA_BASE_URI, "ltc");
	private static final URI LTC_TICKER_URI = URIUtils.resolve(LTC_DATA_BASE_URI, "ticker");
	private static final URI LTC_DEPTH_URI = URIUtils.resolve(LTC_DATA_BASE_URI, "depth");
	private static final URI LTC_TRADES_URI = URIUtils.resolve(LTC_DATA_BASE_URI, "trades");

	private static final String TRADE_API_URL = "https://trade.chbtc.com/api/";

	private static final String METHOD_ORDER = "order";
	private static final String METHOD_CANCEL_ORDER = "cancelOrder";
	private static final String METHOD_GET_ORDER = "getOrder";
	private static final String METHOD_GET_ORDERS = "getOrders";
	private static final String METHOD_GET_ORDERS_NEW = "getOrdersNew";
	private static final String METHOD_GET_ORDERS_IGNORE_TRADE_TYPE = "getOrdersIgnoreTradeType";
	private static final String METHOD_GET_UNFINISHED_ORDERS_IGNORE_TRADE_TYPE = "getUnfinishedOrdersIgnoreTradeType";
	private static final String METHOD_GET_ACCOUNT_INFO = "getAccountInfo";

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private static final DepthReader DEPTH_READER = new DepthReader(OBJECT_MAPPER);

	private static final TypeReference<List<Trade>> TRADE_LIST_TYPE_REFERENCE = new TypeReference<List<Trade>>() {
	};

	private static final TypeReference<List<Order>> ORDER_LIST_TYPE_REFERENCE = new TypeReference<List<Order>>() {
	};

	private static final ValueReader<List<Order>> ORDER_LIST_READER = new ErrorableJsonValueTypeRefReader<>(OBJECT_MAPPER, ORDER_LIST_TYPE_REFERENCE);

	private final Logger log = LoggerFactory.getLogger(CHBTCClient.class);

	private final Map<String, Long> lastReqTime = new HashMap<>(8);

	private final HttpClient httpClient;

	private final String accessKey;

	private final String secretKey;

	@Deprecated
	public CHBTCClient(
			final String username,
			final String password,
			final String safePassword,
			final int socketTimeout,
			final int connectTimeout,
			final int connectionRequestTimeout) {
		httpClient = new HttpClient(
				socketTimeout,
				connectTimeout,
				connectionRequestTimeout);

		this.accessKey = null;
		this.secretKey = null;
	}

	public CHBTCClient(
			final String accessKey,
			final String secretKey,
			final int socketTimeout,
			final int connectTimeout,
			final int connectionRequestTimeout) {
		httpClient = new HttpClient(
				socketTimeout,
				connectTimeout,
				connectionRequestTimeout);

		this.accessKey = accessKey;
		this.secretKey = EncryDigestUtil.digest(secretKey);
	}

	public Ticker getTicker() throws IOException {
		return httpClient.get(TICKER_URI, TickerResponse.class).getTicker();
	}

	public Ticker getTicker(String currency) throws IOException {
		if (currency.equalsIgnoreCase(CURRENCY_LTC)) {
			return httpClient.get(LTC_TICKER_URI, TickerResponse.class)
					.getTicker();
		} else {
			return getTicker();
		}
	}

	public Depth getDepth() throws IOException {
		return httpClient.get(DEPTH_URI, DEPTH_READER);
	}

	public Depth getDepth(String currency) throws IOException {
		if (currency.equalsIgnoreCase(CURRENCY_LTC)) {
			return httpClient.get(LTC_DEPTH_URI, DEPTH_READER);
		} else {
			return getDepth();
		}
	}

	public List<Trade> getTrades() throws IOException {
		return httpClient.get(TRADES_URI, TRADE_LIST_TYPE_REFERENCE);
	}

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

	public List<Trade> getTrades(String currency) throws IOException {
		if (currency.equalsIgnoreCase(CURRENCY_LTC)) {
			return httpClient.get(LTC_TRADES_URI, TRADE_LIST_TYPE_REFERENCE);
		} else {
			return getTrades();
		}
	}

	public List<Trade> getTrades(
			String currency,
			int since
			) throws IOException {
		if (currency.equalsIgnoreCase(CURRENCY_LTC)) {
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

	@Deprecated
	public void login() throws IOException {
	}

	@Deprecated
	public void logout() throws IOException {
	}

	/**
	 * @deprecated Use {@link #order(BigDecimal, BigDecimal, Type, String)} instead.
	 */
	@Deprecated
	public void bid(final BigDecimal unitPrice, BigDecimal btcNumber)
			throws IOException {
		entrust(Type.BUY, unitPrice, btcNumber);
	}

	/**
	 * @deprecated Use {@link #order(BigDecimal, BigDecimal, Type, String)} instead.
	 */
	@Deprecated
	public void ask(final BigDecimal unitPrice, BigDecimal btcNumber)
			throws IOException {
		entrust(Type.SELL, unitPrice, btcNumber);
	}

	/**
	 * @deprecated Use {@link #order(BigDecimal, BigDecimal, Type, String)} instead.
	 */
	@Deprecated
	public void entrust(
			final Type type,
			final BigDecimal unitPrice,
			final BigDecimal btcNumber
			) throws IOException {
		order(unitPrice, btcNumber, type, CURRENCY_BTC);
	}

	/**
	 * Cancel the open order.
	 * @param id the order ID.
	 * @throws IOException indicates I/O exception.
	 * @deprecated Use {@link #cancelOrder(long, String)} instead.
	 */
	@Deprecated
	public void cancel(String id) throws IOException {
		cancelOrder(Long.parseLong(id), CURRENCY_BTC);
	}

	/**
	 * @return
	 * @throws IOException
	 * @deprecated Use {@link #getAccountInfo()} instead.
	 */
	@Deprecated
	public Balance getBalance() throws IOException {
		AccountInfo accountInfo = getAccountInfo();
		Balance balance = new Balance(accountInfo);
		return balance;
	}

	/**
	 * Returns the first page of all entrusts.
	 * @return the first page of all entrusts.
	 * @throws IOException indicates I/O exception.
	 * @deprecated Use {@link #getOrdersIgnoreTradeType(String, int, int)} instead.
	 */
	@Deprecated
	public List<EntrustDetail> getAll() throws IOException {
		return getAll(1);
	}

	/**
	 * Returns all entrusts.
	 * @param page 1 based.
	 * @return all entrusts.
	 * @throws IOException indicates I/O exception.
	 * @deprecated Use {@link #getOrdersIgnoreTradeType(String, int, int)} instead.
	 */
	@Deprecated
	public List<EntrustDetail> getAll(int page) throws IOException {
		return EntrustDetail.toEntrustDetails(
				getOrdersIgnoreTradeType(CURRENCY_BTC, page, 20));
	}

	/**
	 * Returns all buying/selling entrusts.
	 * @return all buying/selling entrusts.
	 * @throws IOException indicates I/O exception.
	 * @deprecated Use {@link #getUnfinishedOrdersIgnoreTradeType(String, int, int)} instead.
	 */
	@Deprecated
	public List<EntrustDetail> getAllBuying() throws IOException {
		List<EntrustDetail> allBuying = new ArrayList<>();

		List<EntrustDetail> buying;
		int page = 1;
		do {
			buying = getBuying(page);
			allBuying.addAll(buying);
			log.debug("Page: {}, record count: {}", page, buying.size());
			page++;
		} while (buying.size() == 10);

		return allBuying;
	}

	/**
	 * Returns the first page of buying/selling entrusts.
	 * @return the first page of buying/selling entrusts.
	 * @throws IOException indicates I/O exception.
	 * @deprecated Use {@link #getUnfinishedOrdersIgnoreTradeType(String, int, int)} instead.
	 */
	@Deprecated
	public List<EntrustDetail> getBuying() throws IOException {
		return getBuying(1);
	}

	/**
	 * Returns buying/selling entrusts.
	 * @param page 1 based
	 * @return buying/selling entrusts.
	 * @throws IOException indicates I/O exception.
	 * @deprecated Use {@link #getUnfinishedOrdersIgnoreTradeType(String, int, int)} instead.
	 */
	@Deprecated
	public List<EntrustDetail> getBuying(int page) throws IOException {
		return EntrustDetail.toEntrustDetails(
				getUnfinishedOrdersIgnoreTradeType(CURRENCY_BTC, 1, page));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
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
	 * @throws IOException
	 */
	public long order(
			final BigDecimal price,
			final BigDecimal amount,
			final Type tradeType,
			final String currency
			) throws IOException {
		OrderResponse orderResponse = get(METHOD_ORDER,
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
	 * @throws IOException
	 */
	public void cancelOrder(
			final long id,
			final String currency
			) throws IOException {
		CHBTCError error = get(METHOD_CANCEL_ORDER,
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
	 * @throws IOException
	 */
	public Order getOrder(
			final long id,
			final String currency
			) throws IOException {
		return get(METHOD_GET_ORDER,
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
	 * @throws IOException
	 */
	public List<Order> getOrders(
			final Type tradeType,
			final String currency,
			final int pageIndex
			) throws IOException {
		return get(METHOD_GET_ORDERS,
				ORDER_LIST_READER,
				new BasicNameValuePair("tradeType",
						String.valueOf(tradeType.getTradeType())),
				new BasicNameValuePair("currency", currency),
				new BasicNameValuePair("pageIndex", String.valueOf(pageIndex)));
	}

	/**
	 * 获取多个委托买单或卖单，每次请求返回 pageSize<=100 条记录。
	 *
	 * @param tradeType 交易类型 1/0[buy/sell]。
	 * @param currency 交易类型(目前仅支持 BTC/LTC)。
	 * @param pageIndex 当前页数。
	 * @param pageSize 每页数量。
	 * @return 多个委托买单或卖单。
	 * @throws IOException
	 */
	public List<Order> getOrdersNew(
			final Type tradeType,
			final String currency,
			final int pageIndex,
			final int pageSize
			) throws IOException {
		return get(METHOD_GET_ORDERS_NEW,
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
	 * @return
	 * @throws IOException
	 */
	public List<Order> getOrdersIgnoreTradeType(
			final String currency,
			final int pageIndex,
			final int pageSize
			) throws IOException {
		return get(METHOD_GET_ORDERS_IGNORE_TRADE_TYPE,
				ORDER_LIST_READER,
				new BasicNameValuePair("currency", currency),
				new BasicNameValuePair("pageIndex", String.valueOf(pageIndex)),
				new BasicNameValuePair("pageSize", String.valueOf(pageSize)));
	}

	/**
	 * 获取未成交或部份成交的买单和卖单，每次请求返回 pageSize<=100 条记录。
	 *
	 * @param currency 交易类型(目前仅支持 BTC/LTC)。
	 * @param pageIndex 当前页数。
	 * @param pageSize 每页数量。
	 * @return 未成交或部份成交的买单和卖单。
	 * @throws IOException
	 */
	public List<Order> getUnfinishedOrdersIgnoreTradeType(
			final String currency,
			final int pageIndex,
			final int pageSize
			) throws IOException {
		return get(METHOD_GET_UNFINISHED_ORDERS_IGNORE_TRADE_TYPE,
				ORDER_LIST_READER,
				new BasicNameValuePair("currency", currency),
				new BasicNameValuePair("pageIndex", String.valueOf(pageIndex)),
				new BasicNameValuePair("pageSize", String.valueOf(pageSize)));
	}

	/**
	 * 获取用户信息。
	 */
	public AccountInfo getAccountInfo() throws IOException {
		return get(METHOD_GET_ACCOUNT_INFO, AccountInfo.class);
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

		final String sign = EncryDigestUtil.hmacSign(params, secretKey);

		final String uri = new StringBuilder(TRADE_API_URL)
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
