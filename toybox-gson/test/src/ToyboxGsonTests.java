import agency.highlysuspect.toybox.gson.ToyboxGson;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class ToyboxGsonTests {
	//a type of datetime string found often in mojang code
	final String odtStr = "2025-04-29T12:30:41+00:00";
	final OffsetDateTime odtParsed = ZonedDateTime.of(2025, 4, 29, 12, 30, 41, 0, ZoneOffset.UTC).toOffsetDateTime();
	
	final Gson gson = ToyboxGson.builder().create();
	
	@Test
	public void testOdtDe() {
		JsonElement elem = new JsonPrimitive(odtStr);
		OffsetDateTime odt = gson.fromJson(elem, OffsetDateTime.class);
		Assertions.assertEquals(odtParsed, odt);
	}
	
	@Test
	public void testOdtSer() {
		JsonElement elem = gson.toJsonTree(odtParsed);
		Assertions.assertInstanceOf(JsonPrimitive.class, elem);
		Assertions.assertEquals(odtStr, elem.getAsString());
	}
}
