package org.oxerr.chbtc.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class EncryDigestUtil {

	private static final Charset ENCODING_CHARSET = StandardCharsets.UTF_8;

	/**
	 * 生成签名消息
	 * @param aValue  要签名的字符串
	 * @param aKey  签名密钥
	 * @return the signature.
	 */
	public static String hmacSign(String aValue, String aKey) {
		byte k_ipad[] = new byte[64];
		byte k_opad[] = new byte[64];
		byte keyb[];
		byte value[];
		keyb = aKey.getBytes(ENCODING_CHARSET);
		value = aValue.getBytes(ENCODING_CHARSET);

		Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
		Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
		for (int i = 0; i < keyb.length; i++) {
			k_ipad[i] = (byte) (keyb[i] ^ 0x36);
			k_opad[i] = (byte) (keyb[i] ^ 0x5c);
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {

			return null;
		}
		md.update(k_ipad);
		md.update(value);
		byte dg[] = md.digest();
		md.reset();
		md.update(k_opad);
		md.update(dg, 0, 16);
		dg = md.digest();
		return toHex(dg);
	}

	public static String toHex(byte input[]) {
		if (input == null) {
			return null;
		}
		StringBuffer output = new StringBuffer(input.length * 2);
		for (byte element : input) {
			int current = element & 0xff;
			if (current < 16) {
				output.append("0");
			}
			output.append(Integer.toString(current, 16));
		}

		return output.toString();
	}

	public static String getHmac(String[] args, String key) {
		if (args == null || args.length == 0) {
			return null;
		}
		StringBuffer str = new StringBuffer();
		for (String arg : args) {
			str.append(arg);
		}
		return hmacSign(str.toString(), key);
	}

	/**
	 * SHA加密
	 * @param aValue string to digest.
	 * @return the digested string.
	 */
	public static String digest(String aValue) {
		aValue = aValue.trim();
		byte value[];
		value = aValue.getBytes(ENCODING_CHARSET);
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		return toHex(md.digest(value));

	}

}
