package agency.highlysuspect.toybox.mapping;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Move all toplevel classes into the provided package, and don't touch any classes already in a package.
 *
 * @see <a href="https://github.com/CrackedPolishedBlackstoneBricksMC/voldeloom/issues/14">this issue</a> for usecase
 */
public class RootRepackager implements Repackager {
	public RootRepackager(String pkg) {
		if(pkg.endsWith("/")) this.pkg = pkg;
		else this.pkg = pkg + "/";
	}
	
	public final String pkg;
	
	@Override
	public @Nullable String repackageOrNull(@NotNull String unpackagedInternalName) {
		return unpackagedInternalName.indexOf('/') == -1 ? pkg + unpackagedInternalName : null;
	}
}
