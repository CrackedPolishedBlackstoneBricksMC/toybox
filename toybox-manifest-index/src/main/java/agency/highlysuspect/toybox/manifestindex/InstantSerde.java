package agency.highlysuspect.toybox.manifestindex;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;

class InstantSerde implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
	@Override
	public JsonElement serialize(Instant instant, Type type, JsonSerializationContext jsonSerializationContext) {
		return new JsonPrimitive(instant.toString());
	}
	
	@Override
	public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return Instant.parse(json.getAsString());
	}
}
