package com.redv.chbtc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 基础信息。
 */
public class Base extends AbstractObject {

	private static final long serialVersionUID = 2014033001L;

	/**
	 * 用户名称。
	 */
	private String username;

	/**
	 * 是否开启安全密码。
	 */
	@JsonProperty("trade_password_enabled")
	private boolean tradePasswordEnabled;

	/**
	 * 是否启用手机认证。
	 */
	@JsonProperty("auth_mobile_enabled")
	private boolean authMobileEnabled;

	/**
	 * 是否启用谷歌认证。
	 */
	@JsonProperty("auth_google_enabled")
	private boolean authGoogleEnabled;

	public String getUsername() {
		return username;
	}

	public boolean isTradePasswordEnabled() {
		return tradePasswordEnabled;
	}

	public boolean isAuthMobileEnabled() {
		return authMobileEnabled;
	}

	public boolean isAuthGoogleEnabled() {
		return authGoogleEnabled;
	}

}
