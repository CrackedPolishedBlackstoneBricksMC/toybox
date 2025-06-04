package agency.highlysuspect.toybox.download;

import java.io.IOException;

public class Downloader {
	public Downloader(DownloadSpec spec, DownloadDest dest, DownloadBackend backend) {
		this.spec = spec;
		this.dest = dest;
		this.backend = backend;
	}
	
	protected final DownloadSpec spec;
	protected final DownloadDest dest;
	protected final DownloadBackend backend;
	
	public void download() throws IOException {
		//TODO: more robust logging facade...
		System.out.println("Downloading " + spec.url + " to " + dest.toString());
		
		if(spec.skipIfExists && dest.exists()) {
			System.out.println("\\-> Already exists at destination");
			return; //all done
		}
		
		try {
			backend.download(spec, dest);
		} catch (Exception e) {
			System.err.println("\\-> Aborting download: " + e.getMessage());
			e.printStackTrace();
			dest.abort();
			return;
		}
		
		System.out.println("\\-> Success");
		dest.success();
		
		//todo verify SHA
	}
}
