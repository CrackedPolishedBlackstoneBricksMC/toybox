package agency.highlysuspect.toybox.manifestindex;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

//TODO maybe make an exception type?
public interface ManifestIndexParser {
	ManifestIndex parseEntireIndex(String string);
	
	default ManifestIndex parseEntireIndex(Path path) throws IOException {
		return parseEntireIndex(new String(Files.readAllBytes(path), StandardCharsets.UTF_8));
	}
	
	default ManifestIndex parseEntireIndex(Reader reader) throws IOException {
		StringBuilder bob = new StringBuilder();
		char[] shuttle = new char[4096];
		int read;
		while((read = reader.read(shuttle)) != -1)
			bob.append(shuttle, 0, read);
		return parseEntireIndex(bob.toString());
	}
	
	//these default to parsing the whole index and picking out one version
	//but subclasses can use incremental/streaming parsers to nudge performance a bit
	//only use these when you really do only want one version!
	
	default ManifestIndex.VersionData parseOneVersion(Reader reader, String wantedVersion) throws IOException {
		return parseEntireIndex(reader).getLinearSearch(wantedVersion);
	}
	
	default ManifestIndex.VersionData parseLatestRelease(Reader reader) throws IOException {
		ManifestIndex mi = parseEntireIndex(reader);
		return mi.getLinearSearch(mi.latest.release);
	}
	
	default ManifestIndex.VersionData parseLatestSnapshot(Reader reader) throws IOException {
		ManifestIndex mi = parseEntireIndex(reader);
		return mi.getLinearSearch(mi.latest.snapshot);
	}
}
