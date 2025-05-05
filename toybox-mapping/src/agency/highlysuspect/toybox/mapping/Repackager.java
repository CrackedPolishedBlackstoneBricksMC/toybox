package agency.highlysuspect.toybox.mapping;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Repackager {
	/**
	 * @param unpackagedInternalName Internal name of the class to repackage, ex. "net/minecraft/src/Block"
	 * @return Internal name of the repackaged class, ex. "net/minecraft/block/Block", or `null` if this repackager has no opinion.
	 */
	@Nullable String repackageOrNull(@NotNull String unpackagedInternalName);
	
	default @NotNull String repackage(@NotNull String unpackagedInternalName) {
		String result = repackageOrNull(unpackagedInternalName);
		return result != null ? result : unpackagedInternalName;
	}
}
