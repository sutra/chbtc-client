package org.oxerr.chbtc;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.oxerr.chbtc.dto.AccountInfo;
import org.oxerr.chbtc.dto.CHBTCError;
import org.oxerr.chbtc.dto.Order;
import org.oxerr.chbtc.dto.OrderResponse;

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
	 * Places a buy or sell order.
	 *
	 * @param method order.
	 * @param accessKey the access key.
	 * @param price the order price.
	 * @param amount the quantity of the order.
	 * @param tradeType the trade type, 1 means buy, 0 means sell.
	 * @param currency the tradable currency, could be BTC, LTC.
	 * @param sign the MD5 signature.
	 * @param reqTime the time stamp of request, in milliseconds.
	 * @return the response of placing order, contains order ID.
	 * @throws IOException indicates I/O exception.
	 * @deprecated Use {@link CHBTCClient#order(java.math.BigDecimal, java.math.BigDecimal, org.oxerr.chbtc.dto.Type, String)} instead.
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
	 * Cancels an order.
	 *
	 * @param method cancelOrder.
	 * @param accessKey the access key.
	 * @param id the ID of the order to be cancelled.
	 * @param currency the currency of the tradable, could be BTC or LTC.
	 * @param sign the MD5 signature.
	 * @param reqTime the time stamp of the request, in milliseconds.
	 * @return the cancellation result.
	 * @throws IOException indicates I/O exception.
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
	 * Gets order detail.
	 *
	 * @param method getOrder.
	 * @param accessKey the access key.
	 * @param id the order ID.
	 * @param currency the currency of the tradable, could be BTC or LTC.
	 * @param sign the MD5 signature.
	 * @param reqTime the time stamp of the reuqest, in milliseconds.
	 * @return the order detail.
	 * @throws IOException indicates I/O exception.
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
	 * Gets orders, the page size is always 10.
	 *
	 * @param method getOrders.
	 * @param accessKey the access key.
	 * @param tradeType the trade type, 1 means buy, 0 means sell.
	 * @param currency the currency of the tradable, could be BTC or LTC.
	 * @param pageIndex the page index, 1 based.
	 * @param sign the MD5 signature.
	 * @param reqTime the time stamp of the request, in milliseconds.
	 * @return order details.
	 * @throws IOException indicates I/O exception.
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
	 * Gets orders, the page size can be specified.
	 *
	 * @param method getOrdersNew.
	 * @param accessKey the access key.
	 * @param tradeType the trade type, 1 means buy, 0 means sell.
	 * @param currency the currency of the tradable, could be BTC or LTC.
	 * @param pageIndex the page index, 1 based.
	 * @param pageSize the page size, should be less than or equals to 100.
	 * @param sign the MD5 signature.
	 * @param reqTime the time stamp of the request, in milliseconds.
	 * @return the orders.
	 * @throws IOException indicates I/O exception.
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
	 * Gets the orders, contains buy and sell order.
	 *
	 * @param method getOrdersIgnoreTradeType.
	 * @param accessKey the access key.
	 * @param currency the currency of the tradable, could be BTC or LTC.
	 * @param pageIndex the page index, 1 based.
	 * @param pageSize the page size.
	 * @param sign the MD5 signature.
	 * @param reqTime the time stamp of the request, in milliseconds.
	 * @return the orders.
	 * @throws IOException indicates I/O exception.
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
	 * Gets open orders, contains buy and sell orders.
	 *
	 * @param method getUnfinishedOrdersIgnoreTradeType.
	 * @param accessKey the access key.
	 * @param currency the currency of the tradable, could be BTC or LTC.
	 * @param pageIndex the page index, 1 based.
	 * @param pageSize the page size, should be less than or equals to 100.
	 * @param sign the MD5 signature.
	 * @param reqTime the time stamp of the request, in milliseconds.
	 * @return the open orders.
	 * @throws IOException indicates I/O exception.
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
	 * Gets account information.
	 *
 	 * @param method getAccountInfo.
	 * @param accessKey the access key.
	 * @param sign the MD5 signature.
	 * @param reqTime the time stamp of the request, in milliseconds.
	 * @return the account information.
	 * @throws IOException indicates I/O exception.
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
