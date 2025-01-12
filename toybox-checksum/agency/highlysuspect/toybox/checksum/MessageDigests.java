package agency.highlysuspect.toybox.checksum;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Supplier;

/**
 * Conveniences for working with MessageDigest objects.
 */
public class MessageDigests {
	/**
	 * Note that SHA1 is not considered secure, but it's okay for insecure checksums.
	 */
	public static final Supplier<MessageDigest> SHA1 = () -> getMessageDigestUnchecked("SHA-1");
	public static final Supplier<MessageDigest> SHA256 = () -> getMessageDigestUnchecked("SHA-256");
	
	public static MessageDigest getMessageDigestUnchecked(String alg) {
		try {
			return MessageDigest.getInstance(alg);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Failed to get message digest " + alg + ": " + e.getMessage(), e);
		}
	}
	
	public static void feedString(MessageDigest dig, String s) {
		dig.update(s.getBytes(StandardCharsets.UTF_8));
	}
	
	public static void feedPath(MessageDigest dig, Path path) throws IOException {
		try(BufferedInputStream in = new BufferedInputStream(Files.newInputStream(path))) {
			feedInputStream(dig, in, 8192); //matching BufferedInputStream.DEFAULT_BUFFER_SIZE
		}
	}
	
	public static void feedInputStream(MessageDigest dig, InputStream in) throws IOException {
		feedInputStream(dig, in, 8192);
	}
	
	public static void feedInputStream(MessageDigest dig, InputStream in, int bufferSize) throws IOException {
		byte[] buf = new byte[bufferSize];
		int read;
		while((read = in.read(buf)) > 0) {
			dig.update(buf, 0, read);
		}
	}
}
