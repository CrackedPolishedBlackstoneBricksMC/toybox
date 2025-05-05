package agency.highlysuspect.toybox.mapping;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A Repackager that fills in missing package mappings for inner classes, such as "net/minecraft/src/Block$1",
 * by attempting to repackage the outer class ("net/minecraft/src/Block") and re-appending the inner class suffix.
 * Can be useful when there are more inner classes than the mappings expect (ex. when doing bytecode-level remapping
 * of old Forges - via source-patching, they introduced inner classes that MCP doesn't have)
 */
public class InnerClassGuessRepackager implements Repackager {
	public InnerClassGuessRepackager(Repackager delegate) {
		this.delegate = delegate;
	}
	
	public final Repackager delegate;
	
	@Override
	public @Nullable String repackageOrNull(@NotNull String unpackagedInternalName) {
		String d = delegate.repackageOrNull(unpackagedInternalName);
		if(d != null) return d;
		
		int money = unpackagedInternalName.indexOf('$');
		if(money == -1) return null; //not an inner class
		
		//If there's no package mapping for "net/minecraft/src/Block$1" try "net/minecraft/src/Block",
		//and if it exists, append the $1 again
		String pfx = delegate.repackageOrNull(unpackagedInternalName.substring(0, money));
		if(pfx != null) return pfx + unpackagedInternalName.substring(money);
		
		//still no clue
		return null;
	}
}
