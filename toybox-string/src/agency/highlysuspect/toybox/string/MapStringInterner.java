package agency.highlysuspect.toybox.string;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MapStringInterner implements StringInterner {
	protected final Map<String, String> cache = new HashMap<>();
	
	@Override
	public @Nullable String intern(@Nullable String in) {
		return in == null ? null : cache.computeIfAbsent(in, Function.identity());
	}
}
