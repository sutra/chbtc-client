package com.redv.chbtc.valuereader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;

import com.redv.chbtc.HttpClient;
import com.redv.chbtc.domain.Root;

public class RootValueReader implements ValueReader<Root> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Root read(InputStream content) throws IOException {
		String string = IOUtils.toString(content, HttpClient.CHBTC_ENCODING);

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