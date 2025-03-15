package agency.highlysuspect.toybox.string;

import org.jetbrains.annotations.Nullable;

public class DummyStringInterner implements StringInterner {
	public static final DummyStringInterner INSTANCE = new DummyStringInterner();
	
	@Override
	public @Nullable String intern(@Nullable String in) {
		return in;
	}
	
	@Override
	public @Nullable String @Nullable [] internArray(@Nullable String @Nullable [] arr) {
		return arr;
	}
}
