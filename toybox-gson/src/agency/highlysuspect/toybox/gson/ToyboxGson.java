package agency.highlysuspect.toybox.gson;

import com.google.gson.GsonBuilder;

import java.time.Instant;
import java.time.OffsetDateTime;

public class ToyboxGson {
	public static GsonBuilder builder() {
		return new GsonBuilder()
			.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerde());
	}
}
