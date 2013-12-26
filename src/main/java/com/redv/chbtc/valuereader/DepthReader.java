package com.redv.chbtc.valuereader;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redv.chbtc.domain.Depth;

/**
 * This class is only to adapt the malformed depth JSON data.
 */
public class DepthReader extends JsonValueReader<Depth>{

	public DepthReader() {
		super(new ObjectMapper(), Depth.class);
	}

	@Override
	protected Depth read(String content) throws IOException {
		String replaced = StringUtils.replace(content, ",[[", ",[");
		return super.read(replaced);
	}

}
