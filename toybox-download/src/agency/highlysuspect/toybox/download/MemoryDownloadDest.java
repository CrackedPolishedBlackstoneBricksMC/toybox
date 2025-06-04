package agency.highlysuspect.toybox.download;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

public class MemoryDownloadDest extends DownloadDest {
	protected ByteArrayOutputStream baos = new ByteArrayOutputStream();
	protected byte[] bytes = null;
	
	@Override
	public OutputStream makeOutputStream() {
		bytes = null;
		baos = new ByteArrayOutputStream();
		return baos;
	}
	
	@Override
	public InputStream getInputStream() {
		if(bytes == null) throw new IllegalStateException("Not downloaded yet!");
		return new ByteArrayInputStream(bytes);
	}
	
	@Override
	public void success() {
		bytes = baos.toByteArray();
		baos = null; //all done with this
	}
	
	@Override
	public void abort() {
		bytes = null;
		baos = null;
	}
	
	@Override
	public boolean exists() {
		return bytes != null;
	}
	
	@Override
	public void digest(MessageDigest digest) {
		if(bytes == null) throw new IllegalStateException("Not downloaded yet!");
		digest.update(bytes);
	}
	
	@Override
	public String toString() {
		return "<memory>";
	}
}
