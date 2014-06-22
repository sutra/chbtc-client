package com.redv.chbtc;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.redv.chbtc.domain.Depth;
import com.redv.chbtc.domain.TickerResponse;
import com.redv.chbtc.domain.Trade;

public interface CHBTCMarketData {

	@GET
	@Path("ticker")
	TickerResponse getTicker();

	@GET
	@Path("depth")
	Depth getDepth();

	@GET
	@Path("trades")
	Trade[] getTrades();

	@GET
	@Path("trades")
	Trade[] getTrades(@QueryParam("since") long since);

}
