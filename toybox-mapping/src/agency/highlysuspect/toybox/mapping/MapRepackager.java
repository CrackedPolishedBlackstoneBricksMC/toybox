package agency.highlysuspect.toybox.mapping;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class MapRepackager implements Repackager {
	public MapRepackager(Map<String, String> packages) {
		this.packages = packages;
	}
	
	//keys: the original class name w/ its package stripped off
	//values: the target package, not including the class name or trailing slash
	//ex. "Block" -> "net/minecraft/block"
	protected Map<String, String> packages;
	
	@Override
	public @Nullable String repackageOrNull(@NotNull String unpackagedInternalName) {
		//remove the entire package prefix from the class
		int lastSlash = unpackagedInternalName.lastIndexOf('/');
		String classNameOnly = lastSlash == -1 ?
			unpackagedInternalName :
			unpackagedInternalName.substring(lastSlash + 1);
		
		//does it go in a package?
		String lookup = packages.get(classNameOnly);
		if(lookup != null) return lookup + "/" + classNameOnly;
		else return null;
	}
}
