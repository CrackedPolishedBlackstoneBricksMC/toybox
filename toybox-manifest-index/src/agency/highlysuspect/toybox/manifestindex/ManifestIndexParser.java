package agency.highlysuspect.toybox.manifestindex;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class ManifestIndexParser {
	public ManifestIndexParser() {
		this.gson = new GsonBuilder()
			.registerTypeAdapter(Instant.class, new InstantSerde())
			.create();
	}
	
	protected Gson gson;
	
	public ManifestIndex parseEntireIndex(Reader reader) {
		return gson.fromJson(reader, ManifestIndex.class);
	}
	
	public ManifestIndex parseEntireIndex(String s)  {
		return gson.fromJson(s, ManifestIndex.class);
	}
	
	public ManifestIndex parseEntireIndex(JsonElement e) {
		return gson.fromJson(e, ManifestIndex.class);
	}
	
	public ManifestIndex parseEntireIndex(Path path) throws IOException {
		try(BufferedReader reader = Files.newBufferedReader(path)) {
			return parseEntireIndex(reader);
		}
	}
	
	//TODO OOOOO completely untested
	
	public ManifestIndex.VersionData streamOneVersion(Reader dataReader, String wantedVersion) throws IOException {
		try(JsonReader reader = new JsonReader(dataReader)) {
			reader.beginObject();
			
			//find versions list
			String name = reader.nextName();
			while(!name.equals("versions")) {
				reader.skipValue();
				name = reader.nextName();
			}
			
			//look inside versions list
			reader.beginArray();
			nextVersion: while(reader.peek() != JsonToken.END_ARRAY) {
				reader.beginObject();
				
				//TODO: allocating in a hot loop?
				ManifestIndex.VersionData candidate = new ManifestIndex.VersionData();
				
				while(reader.peek() != JsonToken.END_OBJECT) {
					switch(reader.nextName()) {
						case "id":
							if(!reader.nextString().equals(wantedVersion)) {
								//not interested in this version
								while(reader.peek() != JsonToken.END_OBJECT) reader.skipValue();
								reader.endObject();
								continue nextVersion;
							}
							candidate.id = wantedVersion;
							break;
						case "type":
							candidate.type = reader.nextString();
							break;
						case "url":
							candidate.url = reader.nextString();
							break;
						case "time":
							candidate.time = Instant.parse(reader.nextString());
							break;
						case "releaseTime":
							candidate.releaseTime = Instant.parse(reader.nextString());
							break;
						case "sha1":
							candidate.sha1 = reader.nextString();
							break;
						case "complianceLevel":
							candidate.complianceLevel = reader.nextInt();
							break;
						default:
							reader.skipValue();
							break;
					}
				}
				
				//survived the above loop without hitting the `continue`: found it
				return candidate;
			}
			
			//didn't find it
			return null;
		}
	}
	
}
