package agency.highlysuspect.toybox.string;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Deduplicates a string.
 */
public interface StringInterner {
	/**
	 * @param in a string to intern
	 * @return an object equal to "in", or "null" if in is null
	 */
	@Contract("null -> null; _ -> param1")
	@Nullable String intern(@Nullable String in);
	
	@Contract("null -> null; _ -> param1")
	default @Nullable String[] internArray(@Nullable String[] arr) {
		if(arr == null) return null;
		
		for(int i = 0; i < arr.length; i++) {
			arr[i] = intern(arr[i]);
		}
		return arr;
	}
}
