package com.redv.chbtc;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.core.type.TypeReference;
import com.redv.chbtc.domain.Balance;
import com.redv.chbtc.domain.Depth;
import com.redv.chbtc.domain.Root;
import com.redv.chbtc.domain.Ticker;
import com.redv.chbtc.domain.TickerResponse;
import com.redv.chbtc.domain.Trade;
import com.redv.chbtc.domain.Type;

public class CHBTCClient implements AutoCloseable{


	private static final URL BASE_URL = newURL("https://www.chbtc.com/");

	private static final URL API_BASE_URL = newURL(BASE_URL, "data/");

	private static final URI TICKER_URI = toURI(newURL(API_BASE_URL, "ticker"));

	private static final URI DEPTH_URI = toURI(newURL(API_BASE_URL, "depth"));

	private static final URI TRADES_URI = toURI(newURL(API_BASE_URL, "trades"));

	private static final URI LOGIN_URI = toURI(newURL(BASE_URL, "user/doLogin"));

	private static final URI ENTRUST_URI = toURI(newURL(BASE_URL, "u/transaction/entrust/doEntrust"));

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

	private HttpClient httpClient;

	private String username;

	private String password;

	public CHBTCClient(final String username, final String password) {
		httpClient = new HttpClient();

		this.username = username;
		this.password = password;
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

	public void entrust(
			final Type type,
			final BigDecimal unitPrice,
			final BigDecimal btcNumber,
			final String safePassword) throws IOException {
		String isBuy = type == Type.BUY ? "1" : "0";
		postRoot(
				ENTRUST_URI,
				new BasicNameValuePair("unitPrice", unitPrice.toString()),
				new BasicNameValuePair("btcNumber", btcNumber.toString()),
				new BasicNameValuePair("safePassword", safePassword),
				new BasicNameValuePair("isBuy", isBuy));
	}

	public Balance getBalance() throws IOException {
		return httpClient.get(GET_BALANCE_URI, new TypeReference<List<Balance>>() {
		}, "jsonp1385317020431").get(0);
	}

	private void postRoot(URI uri, NameValuePair... params) throws IOException {
		final Root root = httpClient.post(uri, params);
		if (!root.isSuccess()) {
			throw new IOException(root.getDes());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		httpClient.close();
	}


}
