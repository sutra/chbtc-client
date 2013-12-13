package com.redv.chbtc;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.http.NameValuePair;
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
import com.redv.chbtc.valuereader.EntrustDetailsReader;

public class CHBTCClient implements AutoCloseable{

	private static final URL BASE_URL = newURL("https://www.chbtc.com/");

	private static final URL API_BASE_URL = newURL(BASE_URL, "data/");

	private static final URI TICKER_URI = toURI(newURL(API_BASE_URL, "ticker"));

	private static final URI DEPTH_URI = toURI(newURL(API_BASE_URL, "depth"));

	private static final URI TRADES_URI = toURI(newURL(API_BASE_URL, "trades"));

	private static final URI LOGIN_URI = toURI(newURL(BASE_URL, "user/doLogin"));

	private static final String LOGOUT_URL = newURL(BASE_URL, "user/logout").toExternalForm();

	private static final URI ENTRUST_URI = toURI(newURL(BASE_URL, "u/transaction/entrust/doEntrust"));

	private static final String CANCEL_URL = newURL(BASE_URL, "u/transaction/EntrustDeatils/doCancle").toExternalForm();

	private static final URI GET_BALANCE_URI = toURI(newURL(BASE_URL, "u/getBalance?jsoncallback=jsonp1385317020431"));

	private static URL newURL(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static URL newURL(URL context, String spec) {
		try {
			return new URL(context, spec);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static URI toURI(URL url) {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

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
		return httpClient.get(DEPTH_URI, Depth.class);
	}

	public List<Trade> getTrades() throws IOException {
		return httpClient.get(TRADES_URI, new TypeReference<List<Trade>>() {
		});
	}

	public List<Trade> getTrades(int since) throws IOException {
		URI uri = toURI(newURL(TRADES_URI.toURL().toExternalForm() + "?since="
				+ since));
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
		postRoot(toURI(newURL(LOGOUT_URL + "?id=" + System.currentTimeMillis())));
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
		String url = String.format("%1$s?id=%2$s&_=%3$d", CANCEL_URL, id, System.currentTimeMillis());
		URI uri = toURI(newURL(url));
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
		URI allURI = toURI(newURL(BASE_URL, spec));
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
		URI buyingURI = toURI(newURL(BASE_URL, spec));
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
