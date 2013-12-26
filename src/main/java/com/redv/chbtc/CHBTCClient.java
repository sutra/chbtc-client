package com.redv.chbtc;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.redv.chbtc.domain.Balance;
import com.redv.chbtc.domain.Depth;
import com.redv.chbtc.domain.EntrustDetail;
import com.redv.chbtc.domain.Root;
import com.redv.chbtc.domain.Ticker;
import com.redv.chbtc.domain.TickerResponse;
import com.redv.chbtc.domain.Trade;
import com.redv.chbtc.domain.Type;
import com.redv.chbtc.valuereader.DepthReader;
import com.redv.chbtc.valuereader.EntrustDetailsReader;

public class CHBTCClient implements AutoCloseable{

	public static final String ENCODING = "UTF-8";

	private static final URI BASE_URI = URI.create("https://www.chbtc.com/");

	private static final URI API_BASE_URI = URIUtils.resolve(BASE_URI, "data/");

	private static final URI TICKER_URI = URIUtils.resolve(API_BASE_URI, "ticker");

	private static final URI DEPTH_URI = URIUtils.resolve(API_BASE_URI, "depth");

	private static final URI TRADES_URI = URIUtils.resolve(API_BASE_URI, "trades");

	private static final URI LOGIN_URI = URIUtils.resolve(BASE_URI, "user/doLogin");

	private static final URI LOGOUT_URI = URIUtils.resolve(BASE_URI, "user/logout");

	private static final URI ENTRUST_URI = URIUtils.resolve(BASE_URI, "u/transaction/entrust/doEntrust");

	private static final URI CANCEL_URI = URIUtils.resolve(BASE_URI, "u/transaction/EntrustDeatils/doCancle");

	private static final URI GET_BALANCE_URI = URIUtils.resolve(BASE_URI, "u/getBalance?jsoncallback=jsonp1385317020431");

	private final Logger log = LoggerFactory.getLogger(CHBTCClient.class);

	private final HttpClient httpClient;

	private final String username;

	private final String password;

	private final String safePassword;

	public CHBTCClient(final String username, final String password) {
		this(username, password, null);
	}

	public CHBTCClient(final String username, final String password, final String safePassword) {
		httpClient = new HttpClient();

		this.username = username;
		this.password = password;
		this.safePassword = safePassword;
	}

	public Ticker getTicker() throws IOException {
		return httpClient.get(TICKER_URI, TickerResponse.class).getTicker();
	}

	public Depth getDepth() throws IOException {
		return httpClient.get(DEPTH_URI, new DepthReader());
	}

	public List<Trade> getTrades() throws IOException {
		return httpClient.get(TRADES_URI, new TypeReference<List<Trade>>() {
		});
	}

	public List<Trade> getTrades(int since) throws IOException {
		final URI uri;
		try {
			uri = new URIBuilder(TRADES_URI).setParameter("since", String.valueOf(since)).build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}

		return httpClient.get(uri, new TypeReference<List<Trade>>() {
		});
	}

	public void login() throws IOException {
		postRoot(
				LOGIN_URI,
				new BasicNameValuePair("nike", username),
				new BasicNameValuePair("pwd", password),
				new BasicNameValuePair("remember", "12"),
				new BasicNameValuePair("safe", "1"));
	}

	public void logout() throws IOException {
		final URI uri;
		try {
			uri = new URIBuilder(LOGOUT_URI)
				.setParameter("id", String.valueOf(System.currentTimeMillis()))
				.build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		postRoot(uri);
	}

	public void bid(final BigDecimal unitPrice, BigDecimal btcNumber)
			throws IOException {
		entrust(Type.BUY, unitPrice, btcNumber);
	}

	public void ask(final BigDecimal unitPrice, BigDecimal btcNumber)
			throws IOException {
		entrust(Type.SELL, unitPrice, btcNumber);
	}

	public void entrust(
			final Type type,
			final BigDecimal unitPrice,
			final BigDecimal btcNumber) throws IOException {
		String isBuy = type == Type.BUY ? "1" : "0";
		List<NameValuePair> params = new ArrayList<>(4);
		params.add(new BasicNameValuePair("unitPrice", unitPrice.toString()));
		params.add(new BasicNameValuePair("btcNumber", btcNumber.toString()));
		if (safePassword != null) {
			params.add(new BasicNameValuePair("safePassword", safePassword));
		}
		params.add(new BasicNameValuePair("isBuy", isBuy));
		postRoot(ENTRUST_URI, params);
	}

	/**
	 * Cancel the open order.
	 * @param id the order ID.
	 * @throws IOException indicates I/O exception.
	 */
	public void cancel(String id) throws IOException {
		final URI uri;
		try {
			uri = new URIBuilder(CANCEL_URI)
					.setParameter("id", id)
					.setParameter("_",
							String.valueOf(System.currentTimeMillis())).build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}

		Root root = doPostRoot(uri);

		if (!root.isSuccess()) {
			if (root.getDes().equals("当前没有可以取消的委托。")) {
				throw new NoCancelableEntrustException(root.getDes());
			} else {
				throw new CHBTCClientException(root.getDes());
			}
		}
	}

	public Balance getBalance() throws IOException {
		return httpClient.get(GET_BALANCE_URI, new TypeReference<List<Balance>>() {
		}, "jsonp1385317020431").get(0);
	}

	/**
	 * Returns the first page of all entrusts.
	 * @return the first page of all entrusts.
	 * @throws IOException indicates I/O exception.
	 */
	public List<EntrustDetail> getAll() throws IOException {
		return getAll(1);
	}

	/**
	 * Returns all entrusts.
	 * @param page 1 based.
	 * @return all entrusts.
	 * @throws IOException indicates I/O exception.
	 */
	public List<EntrustDetail> getAll(int page) throws IOException {
		String spec = String.format("u/transaction/EntrustDeatils/ajaxList-all-%1$d-----?_=%2$d",
				page, System.currentTimeMillis());
		URI allURI = URIUtils.resolve(BASE_URI, spec);
		return httpClient.get(allURI, new EntrustDetailsReader());
	}

	/**
	 * Returns all buying/selling entrusts.
	 * @return all buying/selling entrusts.
	 * @throws IOException indicates I/O exception.
	 */
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
	 */
	public List<EntrustDetail> getBuying() throws IOException {
		return getBuying(1);
	}

	/**
	 * Returns buying/selling entrusts.
	 * @param page 1 based
	 * @return buying/selling entrusts.
	 * @throws IOException indicates I/O exception.
	 */
	public List<EntrustDetail> getBuying(int page) throws IOException {
		String spec = String.format("u/transaction/EntrustDeatils/ajaxList-buying-%1$d-----?_=%2$d",
				page, System.currentTimeMillis());
		URI buyingURI = URIUtils.resolve(BASE_URI, spec);
		return httpClient.get(buyingURI, new EntrustDetailsReader());
	}

	private void postRoot(URI uri, NameValuePair... params) throws IOException {
		postRoot(uri, Arrays.asList(params));
	}

	private void postRoot(URI uri, Collection<NameValuePair> params)
			throws IOException {
		final Root root = doPostRoot(uri, params);
		if (!root.isSuccess()) {
			throw new CHBTCClientException(root.getDes());
		}
	}

	private Root doPostRoot(URI uri, NameValuePair... params) throws IOException {
		return doPostRoot(uri, Arrays.asList(params));
	}

	private Root doPostRoot(URI uri, Collection<NameValuePair> params)
			throws IOException {
		NameValuePair[] array = new NameValuePair[params.size()];
		params.toArray(array);

		final Root root = httpClient.post(uri, array);
		return root;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		logout();
		httpClient.close();
	}

}
