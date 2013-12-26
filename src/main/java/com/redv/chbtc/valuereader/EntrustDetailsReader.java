package com.redv.chbtc.valuereader;

import static com.redv.chbtc.HttpClient.CHBTC_ENCODING;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.redv.chbtc.domain.EntrustDetail;
import com.redv.chbtc.domain.Status;
import com.redv.chbtc.domain.Type;

public class EntrustDetailsReader implements ValueReader<List<EntrustDetail>> {

	private final Logger log = LoggerFactory.getLogger(EntrustDetailsReader.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EntrustDetail> read(InputStream content) throws IOException {
		try {
			return parse(content);
		} catch (SAXException | ParseException e) {
			throw new IOException(e);
		}
	}

	private List<EntrustDetail> parse(InputStream inputStream) throws IOException,
			SAXException, ParseException {
		List<EntrustDetail> entrustDetails = new ArrayList<>();

		HTMLDocument document = toDocument(inputStream);
		HTMLTableElement listTable = (HTMLTableElement) document.getElementById("ListTable");
		HTMLCollection tbodies = listTable.getTBodies();
		for (int i = 0; i < tbodies.getLength(); i++) {
			HTMLTableSectionElement tbody = (HTMLTableSectionElement) tbodies.item(i);
			HTMLCollection rows = tbody.getRows();
			for (int j = 0; j < rows.getLength(); j++) {
				HTMLTableRowElement row = (HTMLTableRowElement) rows.item(j);
				String className = row.getClassName();
				log.debug("Class name: {}", className);
				if (StringUtils.equals(className, "bd")) {
					HTMLCollection cells = row.getCells();
					HTMLTableCellElement dateIdCell = (HTMLTableCellElement) cells.item(0);
					HTMLTableCellElement typeCell = (HTMLTableCellElement) cells.item(1);
					HTMLTableCellElement priceCell = (HTMLTableCellElement) cells.item(2);
					HTMLTableCellElement amountCell = (HTMLTableCellElement) cells.item(3);
					HTMLTableCellElement totalCell = (HTMLTableCellElement) cells.item(4);
					HTMLTableCellElement statusCell = (HTMLTableCellElement) cells.item(5);

					String dateString = dateIdCell.getFirstChild().getTextContent().trim();
					String id = dateIdCell.getChildNodes().item(2).getTextContent().trim();
					String typeString = typeCell.getTextContent().trim();
					String priceString = priceCell.getTextContent().trim();
					String[] prices = priceString.split("/");
					String price = prices[0].substring(1).trim();
					String avgPrice = prices[1].trim();
					String amount = amountCell.getFirstChild().getTextContent().trim().substring(1).trim();
					String filledAmount = amountCell.getChildNodes().item(3).getTextContent().trim();
					String total = totalCell.getFirstChild().getTextContent().trim().substring(1).trim();
					String filled = totalCell.getChildNodes().item(3).getTextContent().trim();
					String statusString = statusCell.getTextContent().trim();

					log.debug("dateString: {}", dateString);
					log.debug("id: {}", id);
					log.debug("typeString: {}", typeString);
					log.debug("price: {}", price);
					log.debug("avgPrice: {}", avgPrice);
					log.debug("amount: {}", amount);
					log.debug("filledAmount: {}", filledAmount);
					log.debug("total: {}", total);
					log.debug("filled: {}", filled);
					log.debug("statusString: {}", statusString);

					Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
					Type type;
					if (typeString.equals("买入")) {
						type = Type.BUY;
					} else if (typeString.equals("卖出")){
						type = Type.SELL;
					} else {
						throw new IllegalArgumentException("Unknown type: " + typeString);
					}

					Status status;
					if (statusString.equals("成功")) {
						status = Status.SUCCESS;
					} else if (statusString.equals("待成交")) {
						status = Status.UNFILLED;
					} else if (statusString.equals("部分成交")) {
						status = Status.PARTIALLY_FILLED;
					} else if (statusString.equals("-")) {
						status = Status.UNKNOWN;
					} else {
						throw new IllegalArgumentException(
								"Unexpected status: " + statusString);
					}

					EntrustDetail detail = new EntrustDetail(
							id,
							date,
							type,
							new BigDecimal(price),
							new BigDecimal(avgPrice),
							new BigDecimal(amount),
							new BigDecimal(filledAmount),
							new BigDecimal(total),
							new BigDecimal(filled),
							status);
					entrustDetails.add(detail);
				}
			}
		}

		return entrustDetails;
	}

	private HTMLDocument toDocument(InputStream inputStream)
			throws IOException, SAXException {
		final InputSource inputSource;
		if (log.isDebugEnabled()) {
			String html = IOUtils.toString(inputStream, CHBTC_ENCODING);
			log.debug("Parsing HTML:\n{}", html);
			inputSource = new InputSource(new InputStreamReader(
					IOUtils.toInputStream(html, CHBTC_ENCODING), CHBTC_ENCODING));
		} else {
			inputSource = new InputSource(new InputStreamReader(inputStream,
					CHBTC_ENCODING));
		}
		DOMParser parser = new DOMParser();
		parser.parse(inputSource);
		HTMLDocument document = (HTMLDocument) parser.getDocument();
		return document;
	}

}
