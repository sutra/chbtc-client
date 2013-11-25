package com.redv.chbtc;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redv.chbtc.domain.Depth;
import com.redv.chbtc.domain.Ticker;
import com.redv.chbtc.domain.TickerResponse;
import com.redv.chbtc.domain.Trade;

public class CHBTCClient implements AutoCloseable{

	private static final URL BASE_URL = getBaseUrl();

	private static URL getBaseUrl() {
		try {
			return new URL("https://www.chbtc.com/data/");
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private CloseableHttpClient httpClient;

	private ObjectMapper objectMapper;

	public CHBTCClient() {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		httpClientBuilder.setRedirectStrategy(new LaxRedirectStrategy());

		String userAgent = "Mozilla/4.0 (compatible; CHBTC Java client)";
		httpClientBuilder.setUserAgent(userAgent);

		Collection<Header> defaultHeaders = new ArrayList<>();
		defaultHeaders.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
		httpClientBuilder.setDefaultHeaders(defaultHeaders);

		httpClient = httpClientBuilder.build();

		objectMapper = new ObjectMapper();
	}

	public Ticker getTicker() throws IOException {
		return get(new URL(BASE_URL, "ticker"), TickerResponse.class).getTicker();
	}

	public Depth getDepth() throws IOException {
		return get(new URL(BASE_URL, "depth"), Depth.class);
	}

	public List<Trade> getTrades() throws IOException {
		return get(new URL(BASE_URL, "trades"), new TypeReference<List<Trade>>() {
		});
	}

	public List<Trade> getTrades(int since) throws IOException {
		return get(new URL(BASE_URL, "trades?since=" + since), new TypeReference<List<Trade>>() {
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		httpClient.close();
	}

	private <T> T get(URL url, final Class<T> valueType) throws IOException {
		return get(url, new ValueReader<T>() {

			@Override
			public T read(InputStream content) throws IOException {
				return objectMapper.readValue(content, valueType);
			}

		});
	}

	private <T> T get(URL url, final TypeReference<T> valueTypeRef) throws IOException {
		return get(url, new ValueReader<T>() {
			@Override
			public T read(InputStream content) throws IOException {
				return objectMapper.readValue(content, valueTypeRef);
			}
		});
	}

	private <T> T get(URL url, ValueReader<T> valueReader) throws IOException {
		HttpGet get;
		try {
			get = new HttpGet(url.toURI());
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}

		try (CloseableHttpResponse response = httpClient.execute(get)) {
			try (InputStream content = response.getEntity().getContent()) {
				return valueReader.read(content);
			}
		}
	}

	private interface ValueReader<T> {

		T read(InputStream content) throws IOException;

	}

}
