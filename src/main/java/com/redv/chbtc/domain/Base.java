package com.redv.chbtc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 基础信息。
 */
public class Base extends AbstractObject {

	private static final long serialVersionUID = 2014063001L;

	/**
	 * 用户名称。
	 */
	private final String username;

	/**
	 * 是否开启安全密码。
	 */
	private final boolean tradePasswordEnabled;

	/**
	 * 是否启用手机认证。
	 */
	private final boolean authMobileEnabled;

	/**
	 * 是否启用谷歌认证。
	 */
	
	private final boolean authGoogleEnabled;

	public Base(
			@JsonProperty("username") final String username,
			@JsonProperty("trade_password_enabled") final boolean tradePasswordEnabled,
			@JsonProperty("auth_mobile_enabled") final boolean authMobileEnabled,
			@JsonProperty("auth_google_enabled") final boolean authGoogleEnabled) {
		this.username = username;
		this.tradePasswordEnabled = tradePasswordEnabled;
		this.authMobileEnabled = authMobileEnabled;
		this.authGoogleEnabled = authGoogleEnabled;
	}

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
