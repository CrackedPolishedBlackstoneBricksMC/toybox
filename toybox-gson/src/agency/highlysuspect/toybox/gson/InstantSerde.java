package agency.highlysuspect.toybox.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;

public class InstantSerde implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
	@Override
	public JsonElement serialize(Instant instant, Type type, JsonSerializationContext jsonSerializationContext) {
		return new JsonPrimitive(instant.toString());
	}
	
	@Override
	public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return Instant.parse(json.getAsString());
	}
}
