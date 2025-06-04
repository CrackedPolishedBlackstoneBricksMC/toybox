package agency.highlysuspect.toybox.download;

import org.jetbrains.annotations.Nullable;

public class DownloadSpec {
	public DownloadSpec() {}
	public DownloadSpec(String url) {
		this.url = url;
	}
	
	public String url;
	
	public boolean skipIfExists = true;
	public boolean useEtag = true;
	public boolean requestGzip = true;
	
	//TODO
	public @Nullable String sha1;
	
	public DownloadSpec url(String url) {
		this.url = url;
		return this;
	}
	
	public DownloadSpec skipIfExists(boolean skipIfExists) {
		this.skipIfExists = skipIfExists;
		return this;
	}
	
	public DownloadSpec useEtag(boolean useEtag) {
		this.useEtag = useEtag;
		return this;
	}
	
	public DownloadSpec requestGzip(boolean requestGzip) {
		this.requestGzip = requestGzip;
		return this;
	}
	
	public DownloadSpec sha1(@Nullable String sha1) {
		this.sha1 = sha1;
		return this;
	}
}
