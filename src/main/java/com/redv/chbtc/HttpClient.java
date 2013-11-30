package com.redv.chbtc;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
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
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redv.chbtc.domain.Root;

public class HttpClient implements AutoCloseable {

	/**
	 * Status code (200) indicating the request succeeded normally.
	 */
	private static final int SC_OK = 200;

	private final Logger log = LoggerFactory.getLogger(HttpClient.class);

	private final CloseableHttpClient httpClient;

	private final ObjectMapper objectMapper;

	public HttpClient() {
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

	public <T> T get(URI uri, final Class<T> valueType) throws IOException {
		return get(uri, new ValueReader<T>() {

			@Override
			public T read(InputStream content) throws IOException {
				return objectMapper.readValue(content, valueType);
			}

		});
	}

	public <T> T get(URI uri, final TypeReference<T> valueTypeRef) throws IOException {
		return get(uri, new ValueReader<T>() {
			@Override
			public T read(InputStream content) throws IOException {
				return objectMapper.readValue(content, valueTypeRef);
			}
		});
	}

	private <T> T get(URI uri, ValueReader<T> valueReader) throws IOException {
		HttpGet get = new HttpGet(uri);
		return execute(uri, valueReader, get);
	}

	public Root post(URI uri, NameValuePair... params) throws IOException {
		ValueReader<Root> rootValueReader = new RootValueReader();
		return post(uri, rootValueReader, params);
	}

	private <T> T post(URI uri, ValueReader<T> valueReader,
			NameValuePair... params) throws IOException {
		return post(uri, valueReader, Arrays.asList(params));
	}

	private <T> T post(URI uri, ValueReader<T> valueReader,
			List<NameValuePair> params) throws IOException {
		HttpPost post = new HttpPost(uri);
		post.setEntity(new UrlEncodedFormEntity(params));
		return execute(uri, valueReader, post);
	}

	private <T> T execute(
			final URI uri,
			final ValueReader<T> valueReader,
			final HttpUriRequest request) throws IOException {
		log.debug("Executing: {}", uri);
		try (CloseableHttpResponse response = httpClient.execute(request)) {
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

	private interface ValueReader<T> {

		T read(InputStream content) throws IOException;

	}

	private class RootValueReader implements ValueReader<Root> {

		/**
		 * {@inheritDoc}
		 */
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

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		httpClient.close();
	}

}
