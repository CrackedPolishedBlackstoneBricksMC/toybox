package agency.highlysuspect.toybox.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class OffsetDateTimeSerde implements JsonSerializer<OffsetDateTime>, JsonDeserializer<OffsetDateTime> {
	static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		.parseCaseInsensitive()
    .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
		//Mojang seems to use +00:00 instead of Z to signify no zone offset.
		//Copy them for a better roundtrip
    .appendOffset("+HH:MM:ss", "+00:00")
		.toFormatter(Locale.ROOT);
	
	@Override
	public JsonElement serialize(OffsetDateTime odt, Type type, JsonSerializationContext jsonSerializationContext) {
		return new JsonPrimitive(formatter.format(odt));
	}
	
	@Override
	public OffsetDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return OffsetDateTime.parse(json.getAsString());
	}
}
