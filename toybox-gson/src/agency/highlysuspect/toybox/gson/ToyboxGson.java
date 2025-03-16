package agency.highlysuspect.toybox.gson;

import com.google.gson.GsonBuilder;

import java.time.Instant;

public class ToyboxGson {
	public static GsonBuilder builder() {
		return new GsonBuilder()
			.registerTypeAdapter(Instant.class, new InstantSerde());
	}
}
