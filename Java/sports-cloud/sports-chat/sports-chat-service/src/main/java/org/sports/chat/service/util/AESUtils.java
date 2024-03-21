package org.sports.chat.service.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


public class AESUtils {

	public static final String CHAR_ENCODING = "UTF-8";
	public static final String AES_ALGORITHM = "AES";

	/**
	 * 加密
	 *
	 * @param data 待加密内容
	 * @param key  加密秘钥
	 * @return 十六进制字符串
	 */
	public static byte[] encrypt(byte[] data, String key) {
		if (key.length() < 16) {
			throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
		} else if (key.length() > 16) {
			key = key.substring(0, 16);
		}

		try {
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, genKey(key));// 初始化
			byte[] result = cipher.doFinal(data);
			return parseByte2HexStr(result).getBytes(StandardCharsets.UTF_8); // 加密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 解密
	 *
	 * @param data 待解密内容(十六进制字符串)
	 * @param key  加密秘钥
	 * @return
	 */
	public static byte[]  decrypt(byte[] data, String key) {
		if (key.length() < 16) {
			throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
		} else if (key.length() > 16) {
			key = key.substring(0, 16);
		}
		try {
			byte[] decryptFrom = parseHexStr2Byte(new String(data));
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, genKey(key));// 初始化
			return cipher.doFinal(decryptFrom); // 加密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 创建加密解密密钥
	 *
	 * @param key 加密解密密钥
	 * @return
	 */
	private static SecretKeySpec genKey(String key) {
		SecretKeySpec secretKey;
		try {
			secretKey = new SecretKeySpec(key.getBytes(CHAR_ENCODING), AES_ALGORITHM);
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, AES_ALGORITHM);
			return seckey;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("genKey fail!", e);
		}
	}

	/**
	 * 将二进制转换成16进制
	 *
	 * @param buf
	 * @return
	 */
	private static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 *
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1) {
			return null;
		}
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		String aesKey = "3667644840989962";
		String originString ="{\"cmd\":5,\"userId\":\"1731947629892354048\",\"token\":\"RTfHswQFOY7RWYdjWdkroP8fgkBVBiqH4oyPcS5a1MBNIsJxGO9CAhWfvto6lPpR\",\"loginType\":3,\"source\":3}";
		byte[] aes = AESUtils.encrypt(originString.getBytes(CHAR_ENCODING), aesKey);
		System.out.println(new String(aes,CHAR_ENCODING));
		String ddd = "6C72B5774DED7AF970E76C3C69EF1034AE3661FA6541FF2356514F6F168518113DCFF37C26498D45B0627B4E81A185F741A7BFE02DFAED955412F80D930BFC1B2D4AABE039B918E73CC7319884322227DB17653D78E4958715E1458C62ACC03426BDA876FD58F1A97C36E247DCA3EA4FCEA3643590C541FB23CD2A97B8F4554C0DE41EA1731DA7D8B2EC4899B3CC8E0A";
		System.out.println(new String(AESUtils.decrypt(ddd.getBytes(), aesKey),CHAR_ENCODING));
	}
}
