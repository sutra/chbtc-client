package org.oxerr.chbtc.dto.valuereader;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.oxerr.chbtc.dto.Depth;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is only to adapt the malformed depth JSON data.
 */
public class DepthReader extends JsonValueReader<Depth>{

	public DepthReader(final ObjectMapper objectMapper) {
		super(objectMapper, Depth.class);
	}

	@Override
	protected Depth read(String content) throws IOException {
		String replaced = StringUtils.replace(content, ",[[", ",[");
		return super.read(replaced);
	}

}
