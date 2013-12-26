package com.redv.chbtc.valuereader;

import static com.redv.chbtc.CHBTCClient.ENCODING;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redv.chbtc.CHBTCClientException;
import com.redv.chbtc.LoginRequiredException;

public class JsonpValueReader<T> implements ValueReader<T> {

	private final Logger log = LoggerFactory.getLogger(JsonpValueReader.class);

	private final String method;

	private final ValueReader<T> valueReader;

	public JsonpValueReader(String method, ValueReader<T> valueReader) {
		this.method = method;
		this.valueReader = valueReader;
	}

	public JsonpValueReader(ObjectMapper objectMapper, String method, TypeReference<T> valueTypeRef) {
		this(method, new JsonValueTypeRefReader<T>(objectMapper, valueTypeRef));
	}

	@Override
	public T read(InputStream inputStream) throws IOException {
		final String content = IOUtils.toString(inputStream, ENCODING);
		final String methodPrefix = method + "(";
		final String json = content.substring(methodPrefix.length(), content.length() - 1);
		log.debug("json: {}", json);
		try {
			return valueReader.read(IOUtils.toInputStream(json, ENCODING));
		} catch (JsonParseException e) {
			if (json.contains("用户登录")) {
				throw new LoginRequiredException();
			} else {
				String message = String.format("%1$s: %2$s",
						e.getLocalizedMessage(), content);
				throw new CHBTCClientException(message, e);
			}
		}
	}

}