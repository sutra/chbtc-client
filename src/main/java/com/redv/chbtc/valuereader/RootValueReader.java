package com.redv.chbtc.valuereader;

import static com.redv.chbtc.CHBTCClient.ENCODING;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;

import com.redv.chbtc.domain.Root;

public class RootValueReader implements ValueReader<Root> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Root read(InputStream content) throws IOException {
		String string = IOUtils.toString(content, ENCODING);

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);
			Unmarshaller um = jaxbContext.createUnmarshaller();
			try (InputStream inputStream = IOUtils.toInputStream(string)) {
				Root root = (Root) um.unmarshal(inputStream);
				return root;
			}
		} catch (JAXBException e) {
			throw new IOException("Unmarshal failed: " + string, e);
		}
	}

}