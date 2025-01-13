package agency.highlysuspect.toybox.versionmanifest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class VersionManifestParser {
	public VersionManifestParser() {
		this.gson = new GsonBuilder()
			.registerTypeAdapter(Instant.class, new InstantSerde())
			.create();
	}
	
	protected Gson gson;
	
	public VersionManifest parseEntireIndex(Reader reader) {
		return gson.fromJson(reader, VersionManifest.class);
	}
	
	public VersionManifest parseEntireIndex(String s)  {
		return gson.fromJson(s, VersionManifest.class);
	}
	
	public VersionManifest parseEntireIndex(JsonElement e) {
		return gson.fromJson(e, VersionManifest.class);
	}
	
	public VersionManifest parseEntireIndex(Path path) throws IOException {
		try(BufferedReader reader = Files.newBufferedReader(path)) {
			return parseEntireIndex(reader);
		}
	}
}
