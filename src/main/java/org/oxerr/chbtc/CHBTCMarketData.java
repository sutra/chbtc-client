package org.oxerr.chbtc;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.oxerr.chbtc.dto.Depth;
import org.oxerr.chbtc.dto.TickerResponse;
import org.oxerr.chbtc.dto.Trade;

public interface CHBTCMarketData {

	@GET
	@Path("ticker")
	TickerResponse getTicker() throws IOException;

	@GET
	@Path("depth")
	Depth getDepth() throws IOException;

	@GET
	@Path("trades")
	Trade[] getTrades() throws IOException;

	@GET
	@Path("trades")
	Trade[] getTrades(@QueryParam("since") long since) throws IOException;

}
