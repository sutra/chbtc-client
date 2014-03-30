package com.redv.chbtc.valuereader;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redv.chbtc.CHBTCClientException;
import com.redv.chbtc.domain.CHBTCError;

public class ErrorableJsonValueTypeRefReader<T> extends JsonValueTypeRefReader<T> {

	private final ObjectMapper objectMapper;

	private final TypeReference<T> valueTypeRef;

	public ErrorableJsonValueTypeRefReader(ObjectMapper objectMapper,
			TypeReference<T> valueTypeRef) {
		super(objectMapper, valueTypeRef);

		this.objectMapper = objectMapper;
		this.valueTypeRef = valueTypeRef;
	}

	@Override
	public T read(final InputStream inputStream) throws IOException {
		String content = IOUtils.toString(inputStream);

		try {
			return objectMapper.readValue(content, valueTypeRef);
		} catch (JsonMappingException e) {
			CHBTCError error = objectMapper.readValue(
					content, CHBTCError.class);
			throw new CHBTCClientException(error);
		}
	}

}
