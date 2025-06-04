package agency.highlysuspect.toybox.download;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PathDownloadDest extends DownloadDest {
	public PathDownloadDest(Path path) {
		this.path = path;
	}
	
	protected final Path path;
	
	protected boolean mkdirs = true;
	protected boolean deleteOnAbort = true;
	protected boolean renameIntoPlace = true;
	
	//Automatically create parent dirs before attempting to download
	//Can be expensive when downloading a lot of files
	public PathDownloadDest mkdirs(boolean mkdirs) {
		this.mkdirs = mkdirs;
		return this;
	}
	
	//Delete the file when DownloadDest#abort is called
	public PathDownloadDest deleteOnAbort(boolean deleteOnAbort) {
		this.deleteOnAbort = deleteOnAbort;
		return this;
	}
	
	//First download the file into a temporary filename (with ".download-temp"
	//suffixed) then rename it to the correct filename
	public PathDownloadDest renameIntoPlace(boolean renameIntoPlace) {
		this.renameIntoPlace = renameIntoPlace;
		return this;
	}
	
	/// path suffixing ///
	protected Path withSuffix(String suffix) {
		return path.resolveSibling(path.getFileName() + suffix);
	}
	
	protected Path downloadDest() {
		if(renameIntoPlace) return withSuffix(".download-temp");
		else return path;
	}
	
	protected Path etagPath() {
		return withSuffix(".etag");
	}
	
	@Override
	public OutputStream makeOutputStream() throws IOException {
		if(mkdirs) {
			Path parent = path.getParent();
			if(parent != null) Files.createDirectories(parent);
		}
		
		return new BufferedOutputStream(Files.newOutputStream(downloadDest()));
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return new BufferedInputStream(Files.newInputStream(path));
	}
	
	@Override
	public void success() throws IOException {
		if(renameIntoPlace) Files.move(downloadDest(), path);
	}
	
	@Override
	public void abort() {
		if(deleteOnAbort) {
			try {
				Files.deleteIfExists(downloadDest());
				Files.deleteIfExists(etagPath());
			} catch (IOException e) {
				//ignored
			}
		}
	}
	
	@Override
	public boolean exists() {
		return Files.exists(path);
	}
	
	@Override
	public boolean notExists() {
		return Files.notExists(path);
	}
	
	@Override
	public @Nullable String getCachedEtag() throws IOException {
		try {
			return Files.readString(etagPath(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			return null;
		}
	}
	
	@Override
	public void cacheEtag(String etag) throws IOException {
		Files.writeString(etagPath(), etag, StandardCharsets.UTF_8);
	}
	
	@Override
	public String toString() {
		return path.toString();
	}
}
