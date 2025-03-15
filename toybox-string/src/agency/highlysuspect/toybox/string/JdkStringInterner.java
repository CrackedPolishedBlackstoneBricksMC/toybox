package agency.highlysuspect.toybox.string;

import org.jetbrains.annotations.Nullable;

public class JdkStringInterner implements StringInterner {
	@Override
	public @Nullable String intern(@Nullable String in) {
		return in == null ? null : in.intern();
	}
}
