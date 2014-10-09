package com.redv.chbtc.service.polling;

import java.io.IOException;
import java.math.BigDecimal;

import com.redv.chbtc.CHBTCAdapters;
import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.NotAvailableFromExchangeException;
import com.xeiam.xchange.NotYetImplementedForExchangeException;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.service.polling.PollingAccountService;

public class CHBTCAccountService extends CHBTCAccountServiceRaw implements
		PollingAccountService {

	/**
	 * @param exchangeSpecification the exchange specification.
	 */
	public CHBTCAccountService(ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountInfo getAccountInfo() throws ExchangeException,
			NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		return CHBTCAdapters.adaptAccountInfo(getCHBTCAccountInfo());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String withdrawFunds(String currency, BigDecimal amount,
			String address) throws ExchangeException,
			NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		throw new NotAvailableFromExchangeException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String requestDepositAddress(String currency, String... args)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		throw new NotAvailableFromExchangeException();
	}

}
