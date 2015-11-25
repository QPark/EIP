package com.qpark.eip.core;

/**
 * @author bhausen
 */
public class ByteUtil {
	/**
	 * @param b
	 *            the byte
	 * @return the signed value of the byte. <code>11111111</code> equals signed
	 *         to <code>-1</code> and unsigned to <code>255</code>.
	 */
	public static int getSignedValue(final byte b) {
		return b;
	}

	/**
	 * @param b
	 *            the byte
	 * @return the unsigned value of the byte. <code>11111111</code> equals
	 *         signed to <code>-1</code> and unsigned to <code>255</code>.
	 */
	public static int getUnsignedValue(final byte b) {
		return b & 0xff;
	}

	public static void main(final String[] args) {
		String s = "11111111";
		byte b;
		b = parseBitString(s);
		System.out.println(b);
		System.out.println(getUnsignedValue(b));
		System.out.println(toBinary(b));
		System.out.println(parseBitString(toBinary(b)));
		System.out.println(toHex(b));
		System.out.println(parseHexString(toHex(b)));
	}

	/**
	 * @param bitString
	 *            the bit string
	 * @return the byte
	 */
	public static byte parseBitString(final String bitString) {
		if (bitString != null && bitString.length() == 8
				&& bitString.matches("^[01]*$")) {
			return (byte) Integer.parseInt(bitString, 2);
		} else {
			throw new IllegalArgumentException(
					"Argument needs to be a string of length 8 only containing 0 and 1.");
		}
	}

	/**
	 * @param hexString
	 *            the hex string.
	 * @return the byte
	 */
	public static byte parseHexString(final String hexString) {
		if (hexString != null && hexString.length() == 2
				&& hexString.matches("^[0-1A-Fa-f]*$")) {
			return (byte) Integer.parseInt(hexString, 16);
		} else {
			throw new IllegalArgumentException(
					"Argument needs to be a string of length 2 only containing digits or the letters from A to F.");
		}
	}

	/**
	 * @param b
	 *            the byte
	 * @return String of length 8 with bit representation of the byte.
	 */
	public static String toBinary(final byte b) {
		return String.format("%8s", Integer.toBinaryString(b & 0xff))
				.replace(' ', '0');
	}

	/**
	 * @param b
	 *            the byte
	 * @return String of length 2 with hexadecimal representation of the byte.
	 */
	public static String toHex(final byte b) {
		return String.format("%2s", Integer.toHexString(b & 0xff))
				.replace(' ', '0').toUpperCase();
	}
}
