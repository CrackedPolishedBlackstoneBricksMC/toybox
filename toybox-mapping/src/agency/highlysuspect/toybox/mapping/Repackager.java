package agency.highlysuspect.toybox.mapping;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface Repackager {
	/**
	 * @param unpackagedInternalName Internal name of the class to repackage, ex. "net/minecraft/src/Block"
	 * @return Internal name of the repackaged class, ex. "net/minecraft/block/Block", or `null` if this repackager has no opinion.
	 */
	@Nullable String repackageOrNull(@NotNull String unpackagedInternalName);
	
	default String repackage(String unpackagedInternalName) {
		String result = repackageOrNull(unpackagedInternalName);
		return result != null ? result : unpackagedInternalName;
	}
	
	default Repackager or(Repackager other) {
		return internal -> {
			String first = repackageOrNull(internal);
			return first != null ? first : other.repackageOrNull(internal);
		};
	}
	
	/**
	 * @return A Repackager that fills in missing package mappings for inner classes, such as "net/minecraft/src/Block$1",
	 * by attempting to repackage the outer class ("net/minecraft/src/Block") and re-appending the inner class suffix.
	 * Can be useful when there are more inner classes than the mappings expect (ex. when doing bytecode-level remapping
	 * of old Forges - they introduced inner classes that MCP doesn't have via source patching)
	 */
	default Repackager withInnerClassGuessing() {
		return this.or(internal -> {
			int money = internal.indexOf('$');
			if(money == -1) return null;
			
			//If there's no package mapping for "net/minecraft/src/Block$1"
			//try "net/minecraft/src/Block"
			String repackagedPrefix = repackageOrNull(internal.substring(0, money));
			//and if it exists, append the $1 again
			if(repackagedPrefix != null) return repackagedPrefix + internal.substring(money);
			else return null;
		});
	}
	
	default Repackager withFallbackPackage(@Nullable String fallback) {
		if(fallback == null || fallback.isEmpty()) return this;
		//TODO is this the right semantics for the default package
		// https://github.com/CrackedPolishedBlackstoneBricksMC/voldeloom/issues/14
		else return this.or(internal -> fallback + "/" + internal);
	}
	
	default Repackager andThen(Function<String, @Nullable String> func) {
		return internal -> func.apply(this.repackageOrNull(internal));
	}
}
