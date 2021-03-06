package org.eclipse.dirigible.api.v3.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.eclipse.dirigible.commons.api.helpers.BytesHelper;

public class HexFacade {

	public static final String encode(byte[] input) {
		return Hex.encodeHexString(input);
	}

	public static final byte[] decode(String input) throws DecoderException {
		return Hex.decodeHex(input.toCharArray());
	}

	public static final String encode(String input) {
		byte[] bytes = BytesHelper.jsonToBytes(input);
		return encode(bytes);
	}

}
