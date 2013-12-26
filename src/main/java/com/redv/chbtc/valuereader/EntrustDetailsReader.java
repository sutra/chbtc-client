package com.redv.chbtc.valuereader;

import static com.redv.chbtc.CHBTCClient.ENCODING;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public List<EntrustDetail> read(InputStream inputStream) throws IOException {
		final String content = IOUtils.toString(inputStream, ENCODING);
		try {
			return parse(content);
		} catch (Exception e) {
			String msg = String.format("Parse entrust details from \"%1$s\" failed.",
					content);
			throw new IOException(msg, e);
		}
	}

	private List<EntrustDetail> parse(String content) throws IOException,
			SAXException, ParseException {
		Pattern p = Pattern.compile("javascript:details\\(([0-9]+)\\);");

		List<EntrustDetail> entrustDetails = new ArrayList<>();

		HTMLDocument document = toDocument(content);
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
					HTMLTableCellElement dateCell = (HTMLTableCellElement) cells.item(0);
					HTMLTableCellElement typeCell = (HTMLTableCellElement) cells.item(1);
					HTMLTableCellElement priceCell = (HTMLTableCellElement) cells.item(2);
					HTMLTableCellElement amountCell = (HTMLTableCellElement) cells.item(3);
					HTMLTableCellElement totalCell = (HTMLTableCellElement) cells.item(4);
					HTMLTableCellElement statusCell = (HTMLTableCellElement) cells.item(5);
					HTMLTableCellElement operationCell = (HTMLTableCellElement) cells.item(6);

					String hrefForId = operationCell.getElementsByTagName("a").item(0).getAttributes().getNamedItem("href").getNodeValue();
					log.debug("hrefForId: {}", hrefForId);

					String dateString = dateCell.getFirstChild().getTextContent().trim();
					String typeString = typeCell.getTextContent().trim();
					String priceString = priceCell.getTextContent().trim();
					String[] prices = priceString.split("/");
					String price = prices[0].substring(1).trim();
					String avgPrice = prices[1].trim();
					String amount = amountCell.getFirstChild().getTextContent().trim().substring(1).trim();
					amount = amount.substring(0, amount.length() - amount.indexOf("/")).trim();
					String filledAmount = amountCell.getChildNodes().item(1).getTextContent().trim();
					String total = totalCell.getFirstChild().getTextContent().trim().substring(1).trim();
					total = total.substring(0, total.length() - total.indexOf("/")).trim();
					String filled = totalCell.getChildNodes().item(1).getTextContent().trim();
					String statusString = statusCell.getTextContent().trim();
					Matcher m = p.matcher(hrefForId);
					final String id;
					if (m.matches()) {
						id = m.group(1);
					} else {
						id = null;
					}
					log.debug("dateString: {}", dateString);
					log.debug("typeString: {}", typeString);
					log.debug("price: {}", price);
					log.debug("avgPrice: {}", avgPrice);
					log.debug("amount: {}", amount);
					log.debug("filledAmount: {}", filledAmount);
					log.debug("total: {}", total);
					log.debug("filled: {}", filled);
					log.debug("statusString: {}", statusString);
					log.debug("id: {}", id);

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

	private HTMLDocument toDocument(String content)
			throws UnsupportedEncodingException, IOException, SAXException {
		final InputSource inputSource;
		inputSource = new InputSource(new InputStreamReader(
				IOUtils.toInputStream(content, ENCODING), ENCODING));

		DOMParser parser = new DOMParser();
		parser.parse(inputSource);
		HTMLDocument document = (HTMLDocument) parser.getDocument();
		return document;
	}

}
