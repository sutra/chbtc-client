package com.redv.chbtc;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.redv.chbtc.domain.AccountInfo;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface CHBTC {

	@GET
	@Path("getAccountInfo")
	AccountInfo getAccountInfo(
			@QueryParam("method") String method,
			@QueryParam("accesskey") String accessKey,
			@QueryParam("sign") String sign,
			@QueryParam("reqTime") long reqTime);

}
