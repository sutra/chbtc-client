package org.oxerr.chbtc.dto.valuereader;

import java.io.IOException;
import java.io.InputStream;

public interface ValueReader<T> {

	T read(InputStream content) throws IOException;

}