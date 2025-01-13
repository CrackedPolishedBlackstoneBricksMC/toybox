package agency.highlysuspect.toybox.versionmanifest;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;

//TODO: copied from toybox-manifest-index
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
