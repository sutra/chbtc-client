package org.oxerr.chbtc;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface CHBTCMarketDataBTC extends CHBTCMarketData {
}
