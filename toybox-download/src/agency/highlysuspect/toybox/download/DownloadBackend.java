package agency.highlysuspect.toybox.download;

import java.io.IOException;

public interface DownloadBackend {
	void download(DownloadSpec spec, DownloadDest dest) throws IOException;
	
	default Downloader newDownloader(DownloadSpec spec, DownloadDest dest) {
		return new Downloader(spec, dest, this);
	}
}
