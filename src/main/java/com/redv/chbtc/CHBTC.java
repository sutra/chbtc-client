package com.redv.chbtc;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.redv.chbtc.domain.AccountInfo;
import com.redv.chbtc.domain.CHBTCError;
import com.redv.chbtc.domain.Order;
import com.redv.chbtc.domain.OrderResponse;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface CHBTC {

	static final String METHOD_ORDER = "order";
	static final String METHOD_CANCEL_ORDER = "cancelOrder";
	static final String METHOD_GET_ORDER = "getOrder";
	static final String METHOD_GET_ORDERS = "getOrders";
	static final String METHOD_GET_ORDERS_NEW = "getOrdersNew";
	static final String METHOD_GET_ORDERS_IGNORE_TRADE_TYPE = "getOrdersIgnoreTradeType";
	static final String METHOD_GET_UNFINISHED_ORDERS_IGNORE_TRADE_TYPE = "getUnfinishedOrdersIgnoreTradeType";
	static final String METHOD_GET_ACCOUNT_INFO = "getAccountInfo";

	/**
	 * @deprecated Use {@link CHBTCClient#order(java.math.BigDecimal, java.math.BigDecimal, com.redv.chbtc.domain.Type, String)} instead.
	 */
	@Deprecated
	@GET
	@Path("order")
	OrderResponse order(
			@QueryParam("method") String method,
			@QueryParam("accesskey") String accessKey,
			@QueryParam("price") String price,
			@QueryParam("amount") String amount,
			@QueryParam("tradeType") int tradeType,
			@QueryParam("currency") String currency,
			@QueryParam("sign") String sign,
			@QueryParam("reqTime") long reqTime)
					throws IOException;

	/**
	 * @deprecated Use {@link CHBTCClient#cancelOrder(long, String)} instead.
	 */
	@Deprecated
	@GET
	@Path("cancelOrder")
	CHBTCError cancelOrder(
			@QueryParam("method") String method,
			@QueryParam("accesskey") String accessKey,
			@QueryParam("id") long id,
			@QueryParam("currency") String currency,
			@QueryParam("sign") String sign,
			@QueryParam("reqTime") long reqTime)
					throws IOException;

	/**
	 * @deprecated Use {@link CHBTCClient#getOrder(long, String)} instead.
	 */
	@Deprecated
	@GET
	@Path("getOrder")
	Order getOrder(
			@QueryParam("method") String method,
			@QueryParam("accesskey") String accessKey,
			@QueryParam("id") long id,
			@QueryParam("currency") String currency,
			@QueryParam("sign") String sign,
			@QueryParam("reqTime") long reqTime)
					throws IOException;

	/**
	 * @deprecated The response JSON maybe array
	 * or object<code>{"code":3001,"message":"挂单没有找到"}</code>,
	 * then it will be parsed failed.
	 */
	@GET
	@Path("getOrders")
	Order[] getOrders(
			@QueryParam("method") String method,
			@QueryParam("accesskey") String accessKey,
			@QueryParam("tradeType") int tradeType,
			@QueryParam("currency") String currency,
			@QueryParam("pageIndex") int pageIndex,
			@QueryParam("sign") String sign,
			@QueryParam("reqTime") long reqTime)
					throws IOException;

	/**
	 * @deprecated The response JSON maybe array
	 * or object<code>{"code":3001,"message":"挂单没有找到"}</code>,
	 * then it will be parsed failed.
	 */
	@GET
	@Path("getOrdersNew")
	Order[] getOrdersNew(
			@QueryParam("method") String method,
			@QueryParam("accesskey") String accessKey,
			@QueryParam("tradeType") int tradeType,
			@QueryParam("currency") String currency,
			@QueryParam("pageIndex") int pageIndex,
			@QueryParam("pageSize") int pageSize,
			@QueryParam("sign") String sign,
			@QueryParam("reqTime") long reqTime)
					throws IOException;

	/**
	 * @deprecated The response JSON may be array
	 * or object<code>{"code":3001,"message":"挂单没有找到"}</code>,
	 * then it will be parsed failed.
	 */
	@GET
	@Path("getOrdersIgnoreTradeType")
	Order[] getOrdersIgnoreTradeType(
			@QueryParam("method") String method,
			@QueryParam("accesskey") String accessKey,
			@QueryParam("currency") String currency,
			@QueryParam("pageIndex") int pageIndex,
			@QueryParam("pageSize") int pageSize,
			@QueryParam("sign") String sign,
			@QueryParam("reqTime") long reqTime)
					throws IOException;

	/**
	 * @deprecated The response JSON maybe array
	 * or object<code>{"code":3001,"message":"挂单没有找到"}</code>,
	 * then it will be parsed failed.
	 */
	@GET
	@Path("getUnfinishedOrdersIgnoreTradeType")
	Order[] getUnfinishedOrdersIgnoreTradeType(
			@QueryParam("method") String method,
			@QueryParam("accesskey") String accessKey,
			@QueryParam("currency") String currency,
			@QueryParam("pageIndex") int pageIndex,
			@QueryParam("pageSize") int pageSize,
			@QueryParam("sign") String sign,
			@QueryParam("reqTime") long reqTime)
					throws IOException;

	/**
	 * @deprecated Use {@link CHBTCClient#getAccountInfo()} instead.
	 */
	@Deprecated
	@GET
	@Path("getAccountInfo")
	AccountInfo getAccountInfo(
			@QueryParam("method") String method,
			@QueryParam("accesskey") String accessKey,
			@QueryParam("sign") String sign,
			@QueryParam("reqTime") long reqTime)
					throws IOException;

}
