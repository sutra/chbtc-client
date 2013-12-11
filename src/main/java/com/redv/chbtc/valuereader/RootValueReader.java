package com.redv.chbtc.valuereader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.redv.chbtc.domain.Root;

public class RootValueReader implements ValueReader<Root> {

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