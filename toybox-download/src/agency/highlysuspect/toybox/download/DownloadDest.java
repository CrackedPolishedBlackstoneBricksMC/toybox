package agency.highlysuspect.toybox.download;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

public abstract class DownloadDest {
	//byte sink, for the downloader
	public abstract OutputStream makeOutputStream() throws IOException;
	
	//byte source, for consuming the downloaded result
	public abstract InputStream getInputStream() throws IOException;
	
	//successfully downloaded the file
	public abstract void success() throws IOException;
	
	//something bad happened, clean up to avoid poisoning the cache
	//TODO: for hash-mismatches, maybe keep the file around (renamed) instead of deleting it?
	//TODO: resuming downloads with range-requests? (requires data to flow back from the sink to the source)
	public abstract void abort();
	
	//does the file exist
	public boolean exists() {
		return false;
	}
	
	//does the file *not* exist - see the difference between Files.exists and Files.notExists
	public boolean notExists() {
		return !exists();
	}
	
	//feed the file to the message digest
	//the file must exist
	public void digest(MessageDigest digest) throws IOException {
		try(InputStream in = getInputStream()) {
			byte[] shuttle = new byte[8192]; //matching BufferedInputStream.DEFAULT_BUFFER_SIZE
			int amt;
			while((amt = in.read(shuttle)) != -1) digest.update(shuttle, 0, amt);
		}
	}
	
	public @Nullable String getCachedEtag() throws IOException {
		return null;
	}
	
	public void cacheEtag(String etag) throws IOException {
	
	}
}
