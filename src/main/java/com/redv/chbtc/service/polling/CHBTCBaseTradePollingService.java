package com.redv.chbtc.service.polling;

import org.apache.http.NameValuePair;

import si.mazi.rescu.RestProxyFactory;

import com.redv.chbtc.CHBTC;
import com.redv.chbtc.util.EncryDigestUtil;
import com.xeiam.xchange.ExchangeSpecification;

public class CHBTCBaseTradePollingService extends CHBTCBasePollingService {

	protected final CHBTC chbtc;

	protected final String accessKey;

	private final String secret;

	/**
	 * @param exchangeSpecification
	 */
	protected CHBTCBaseTradePollingService(
			ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
		final String baseUrl = exchangeSpecification.getSslUri();
		chbtc = RestProxyFactory.createProxy(CHBTC.class, baseUrl);
		accessKey = exchangeSpecification.getApiKey();
		final String secretKey = exchangeSpecification.getSecretKey();
		secret = EncryDigestUtil.digest(secretKey);
	}

	protected String sign(String method, NameValuePair... nameValuePairs) {
		final StringBuilder paramsBuilder =
				new StringBuilder("method=").append(method)
				.append("&accesskey=").append(accessKey);

		for (NameValuePair pair : nameValuePairs) {
			paramsBuilder
				.append("&")
				.append(pair.getName()).append("=").append(pair.getValue());
		}

		final String params = paramsBuilder.toString();

		return EncryDigestUtil.hmacSign(params, secret);
	}

	protected long getReqTime() {
		return System.currentTimeMillis();
	}

}
