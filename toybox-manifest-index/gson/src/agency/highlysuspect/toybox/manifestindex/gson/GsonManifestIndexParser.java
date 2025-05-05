package agency.highlysuspect.toybox.manifestindex.gson;

import agency.highlysuspect.toybox.gson.ToyboxGson;
import agency.highlysuspect.toybox.manifestindex.ManifestIndex;
import agency.highlysuspect.toybox.manifestindex.ManifestIndexParser;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;

public class GsonManifestIndexParser implements ManifestIndexParser {
	public GsonManifestIndexParser() {
		this.gson = ToyboxGson.builder().create();
	}
	
	protected Gson gson;
	
	@Override
	public ManifestIndex parseEntireIndex(Reader reader) {
		return gson.fromJson(reader, ManifestIndex.class);
	}
	
	@Override
	public ManifestIndex parseEntireIndex(String s)  {
		return gson.fromJson(s, ManifestIndex.class);
	}
	
	@Override
	public ManifestIndex parseEntireIndex(Path path) throws IOException {
		try(BufferedReader reader = Files.newBufferedReader(path)) {
			return parseEntireIndex(reader);
		}
	}
	
	public ManifestIndex parseEntireIndex(JsonElement e) {
		return gson.fromJson(e, ManifestIndex.class);
	}
	
	@Override
	public ManifestIndex.VersionData parseVersion(Reader dataReader, String wantedVersion) throws IOException {
		try(JsonReader reader = new JsonReader(dataReader)) {
			reader.beginObject();
			goTo(reader, "versions");
			return scanVersionsArray(reader, wantedVersion);
		}
	}
	
	@Override
	public ManifestIndex.Latest parseLatestVersions(Reader dataReader) throws IOException {
		try(JsonReader reader = new JsonReader(dataReader)) {
			reader.beginObject();
			goTo(reader, "latest");
			reader.beginObject();
			
			ManifestIndex.Latest latest = new ManifestIndex.Latest();
			while(reader.peek() != JsonToken.END_OBJECT) {
				switch(reader.nextName()) {
					case "release": latest.release = reader.nextString(); break;
					case "snapshot": latest.snapshot = reader.nextString(); break;
					default: reader.skipValue();
				}
			}
			
			return latest;
		}
	}
	
	protected void goTo(JsonReader reader, String key) throws IOException {
		while(reader.peek() != JsonToken.END_OBJECT) {
			if(reader.nextName().equals(key)) return; //found it
			reader.skipValue();
		}
		throw new IllegalArgumentException("couldn't find '" + key + "' key");
	}
	
	protected ManifestIndex.VersionData scanVersionsArray(JsonReader reader, String wantedVersion) throws IOException {
		reader.beginArray();
		nextVersion: while(reader.peek() != JsonToken.END_ARRAY) {
			reader.beginObject();
			
			String type = null;
			String url = null;
			OffsetDateTime time = null;
			OffsetDateTime releaseTime = null;
			String sha1 = null;
			int complianceLevel = 0;
			
			while(reader.peek() != JsonToken.END_OBJECT) {
				switch(reader.nextName()) {
					case "id":
						if(!reader.nextString().equals(wantedVersion)) {
							//not interested in this version
							while(reader.peek() != JsonToken.END_OBJECT) reader.skipValue();
							reader.endObject();
							continue nextVersion;
						}
						break;
					case "type":
						type = reader.nextString();
						break;
					case "url":
						url = reader.nextString();
						break;
					case "time":
						time = OffsetDateTime.parse(reader.nextString());
						break;
					case "releaseTime":
						releaseTime = OffsetDateTime.parse(reader.nextString());
						break;
					case "sha1":
						sha1 = reader.nextString();
						break;
					case "complianceLevel":
						complianceLevel = reader.nextInt();
						break;
					default:
						reader.skipValue(); //unknown key
						break;
				}
			}
			
			//survived the loop without hitting the 'continue'; this is the right version
			ManifestIndex.VersionData vd = new ManifestIndex.VersionData();
			vd.id = wantedVersion;
			vd.type = type;
			vd.url = url;
			vd.time = time;
			vd.releaseTime = releaseTime;
			vd.sha1 = sha1;
			vd.complianceLevel = complianceLevel;
			return vd;
		}
		
		return null; //didnt find it
	}
}
