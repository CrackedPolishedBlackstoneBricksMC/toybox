package agency.highlysuspect.toybox.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class HttpURLConnectionBackend implements DownloadBackend {
	protected String userAgent = "Toybox downloader (from Java)";
	
	public HttpURLConnectionBackend userAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}
	
	@Override
	public void download(DownloadSpec spec, DownloadDest dest) throws IOException {
		URL url;
		try {
			url = new URI(spec.url).toURL();
		} catch (URISyntaxException e) {
			throw new IOException("Malformed URL", e);
		}
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestProperty("User-Agent", userAgent);
		if(spec.requestGzip) conn.setRequestProperty("Accept-Encoding", "gzip");
		String cachedEtag = dest.getCachedEtag();
		if(cachedEtag != null) conn.setRequestProperty("If-None-Match", cachedEtag);
		
		//actually make the connection
		System.out.println("\\-> Making connection...");
		conn.connect();
		
		//check response code: 304 is okay if we're using etags, otherwise i want a 2xx
		int code = conn.getResponseCode();
		if(spec.useEtag && code == HttpURLConnection.HTTP_NOT_MODIFIED) {
			System.out.println("\\-> Etag match! Not downloading the rest.");
			return;
		} else if(code / 100 != 2) {
			throw new IOException("Got " + code + " " + conn.getResponseMessage() + " from connection to " + url);
		}
		
		//shuttle the bytes to the destination
		InputStream in;
		if("gzip".equals(conn.getContentEncoding())) in = new GZIPInputStream(conn.getInputStream());
		else in = conn.getInputStream();
		try(in; OutputStream out = dest.makeOutputStream()) {
			byte[] shuttle = new byte[8192];
			int a;
			while((a = in.read(shuttle)) != -1) out.write(shuttle, 0, a);
		}
		
		//save the etag if we got one
		String realEtag = conn.getHeaderField("ETag");
		if(spec.useEtag && realEtag != null) {
			System.out.println("\\-> Got etag: " + realEtag);
			dest.cacheEtag(realEtag);
		}
	}
}
