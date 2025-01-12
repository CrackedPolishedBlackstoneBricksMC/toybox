package agency.highlysuspect.toybox.checksum;

import java.security.MessageDigest;

/**
 * Utilities for printing hashes.
 *
 * @apiNote In Java 17, consider using the HexFormat class instead.
 */
public class HexHashFormatter {
	public HexHashFormatter() {}
	
	/**
	 * Converts the start of a byte array into hexadecimal form, the familiar format used to present hashes to people.
	 * The string will have up-to as many characters as {@code prefixLength}, with one byte in the hash corresponding
	 * to two characters of the string. Subsequent bytes will be skipped.
	 */
	public String format(byte[] data, int prefixLength) {
		StringBuilder out = new StringBuilder(prefixLength);
		for(byte b : data) {
			int hi = (b & 0xF0) >>> 4;
			if(hi <= 9) out.append((char) ('0' + hi));
			else out.append((char) ('a' + hi - 10));
			
			if(out.length() == prefixLength) return out.toString();
			
			int lo = (b & 0x0F);
			if(lo <= 9) out.append((char) ('0' + lo));
			else out.append((char) ('a' + lo - 10));
			
			if(out.length() == prefixLength) return out.toString();
		}
		return out.toString();
	}
	
	public String format(byte[] data) {
		return format(data, data.length * 2);
	}
	
	public String format(MessageDigest digest, int prefixLength) {
		return format(digest.digest(), prefixLength);
	}
	
	public String format(MessageDigest digest) {
		return format(digest, digest.getDigestLength() * 2);
	}
}
