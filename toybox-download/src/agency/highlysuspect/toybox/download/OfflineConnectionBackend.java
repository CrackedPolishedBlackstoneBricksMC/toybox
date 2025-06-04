package agency.highlysuspect.toybox.download;

import java.io.IOException;

public class OfflineConnectionBackend implements DownloadBackend {
	@Override
	public void download(DownloadSpec spec, DownloadDest dest) throws IOException {
		if(dest.notExists()) throw new IOException("Can't download " + spec.url + " to " + dest + " since I'm an OfflineConnectionBackend");
	}
}
