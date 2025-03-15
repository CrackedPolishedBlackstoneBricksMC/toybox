package agency.highlysuspect.toybox.mapping;

import org.jetbrains.annotations.NotNull;

public class DummyRepackager implements Repackager {
	@Override
	public String repackageOrNull(@NotNull String unpackagedInternalName) {
		return unpackagedInternalName;
	}
}
