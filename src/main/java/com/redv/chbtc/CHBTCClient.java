package com.redv.chbtc;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redv.chbtc.domain.Depth;
import com.redv.chbtc.domain.Root;
import com.redv.chbtc.domain.Ticker;
import com.redv.chbtc.domain.TickerResponse;
import com.redv.chbtc.domain.Trade;

public class CHBTCClient implements AutoCloseable{

	/**
	 * Status code (200) indicating the request succeeded normally.
	 */
	private static final int SC_OK = 200;

	private static final URL BASE_URL = newURL("https://www.chbtc.com/");

	private static final URL API_BASE_URL = newURL(BASE_URL, "data/");

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

	private CloseableHttpClient httpClient;

	private ObjectMapper objectMapper;

	private String username;

	private String password;

	public CHBTCClient(final String username, final String password) {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		httpClientBuilder.setRedirectStrategy(new LaxRedirectStrategy());

		String userAgent = "Mozilla/4.0 (compatible; CHBTC Java client)";
		httpClientBuilder.setUserAgent(userAgent);

		Collection<Header> defaultHeaders = new ArrayList<>();
		defaultHeaders.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
		httpClientBuilder.setDefaultHeaders(defaultHeaders);

		httpClient = httpClientBuilder.build();

		objectMapper = new ObjectMapper();

		this.username = username;
		this.password = password;
	}

	public Ticker getTicker() throws IOException {
		return get(new URL(API_BASE_URL, "ticker"), TickerResponse.class).getTicker();
	}

	public Depth getDepth() throws IOException {
		return get(new URL(API_BASE_URL, "depth"), Depth.class);
	}

	public List<Trade> getTrades() throws IOException {
		return get(new URL(API_BASE_URL, "trades"), new TypeReference<List<Trade>>() {
		});
	}

	public List<Trade> getTrades(int since) throws IOException {
		return get(new URL(API_BASE_URL, "trades?since=" + since), new TypeReference<List<Trade>>() {
		});
	}

	public void login() throws IOException {
		final Root root = post(
				new URL(BASE_URL, "user/doLogin"),
				new BasicNameValuePair("nike", username),
				new BasicNameValuePair("pwd", password),
				new BasicNameValuePair("remember", "12"),
				new BasicNameValuePair("safe", "1"));
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
		HttpGet get = new HttpGet(toURI(url));

		try (CloseableHttpResponse response = httpClient.execute(get)) {
			try (InputStream content = response.getEntity().getContent()) {
				return valueReader.read(content);
			}
		}
	}

	private Root post(URL url, NameValuePair... params) throws IOException {
		return post(url, new ValueReader<Root>() {

			@Override
			public Root read(InputStream content) throws IOException {
				try {
					JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);
					Unmarshaller um = jaxbContext.createUnmarshaller();
					Root root = (Root) um.unmarshal(content);
					return root;
				} catch (JAXBException e) {
					throw new IOException(e);
				}
			}

		},params);
	}

	private <T> T post(URL url, ValueReader<T> valueReader,
			NameValuePair... params) throws IOException {
		return post(url, valueReader, Arrays.asList(params));
	}

	private <T> T post(URL url, ValueReader<T> valueReader,
			List<NameValuePair> params) throws IOException {
		HttpPost post = new HttpPost(toURI(url));
		post.setEntity(new UrlEncodedFormEntity(params));

		try (CloseableHttpResponse response = httpClient.execute(post)) {
			final StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == SC_OK) {
				try (InputStream content = response.getEntity().getContent()) {
					return valueReader.read(content);
				}
			} else {
				throw new IOException(statusLine.getReasonPhrase());
			}
		}
	}

	private URI toURI(URL url) throws IOException {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}

	private interface ValueReader<T> {

		T read(InputStream content) throws IOException;

	}

}
