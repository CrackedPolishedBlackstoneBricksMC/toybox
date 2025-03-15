package agency.highlysuspect.toybox.mcp;

import agency.highlysuspect.toybox.string.StringInterner;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Packages {
	public Packages() {
		this(new HashMap<>());
	}
	
	public Packages(@NotNull Map<String, String> packages) {
		this.packages = packages;
	}
	
	protected final Map<String, String> packages;
	
	@Contract("_, _ -> this")
	public Packages read(Path path, StringInterner mem) throws IOException {
		return read(Files.readAllLines(path), mem);
	}
	
	@Contract("_, _ -> this")
	public Packages read(Iterable<String> lines, StringInterner mem) {
		int lineNo = 0;
		for(String lineUntrim : lines) {
			lineNo++;
			String line = lineUntrim.trim();
			
			//skip blanks
			if(line.isEmpty()) continue;
			
			//skip CSV header
			if(lineNo == 1 && "class,package".equals(line)) continue;
			
			//this format doesn't allow comments
			
			String[] split = mem.internArray(line.split(",", 2));
			if(split.length != 2) throw new IllegalStateException("line " + lineNo + " has weird number of elements: " + line);
			
			packages.put(split[0], split[1]);
		}
		
		return this;
	}
	
	public void mergeWith(Packages other) {
		packages.putAll(other.packages);
	}
	
	public boolean isEmpty() {
		return packages.isEmpty();
	}
	
	//internal-name to internal-name
	//TODO: https://github.com/CrackedPolishedBlackstoneBricksMC/voldeloom/issues/14
	// might need a notion of "default package"
	public String repackage(String srgClass) {
		//remove the entire package prefix from the class
		int lastSlash = srgClass.lastIndexOf('/');
		String srgClassNameOnly = lastSlash == -1 ?
			srgClass :
			srgClass.substring(lastSlash + 1);
		
		//values of the map are the new package this class should go in
		String lookup = packages.get(srgClassNameOnly);
		if(lookup != null) return lookup + "/" + srgClassNameOnly;
		
		//ok, maybe we're remapping an inner class which doesn't exist in the package mappings.
		//happens sometimes if there are more inner classes in the binary than in the source.
		//guess a name by repackaging the base class without the $xxx suffix.
		int money = srgClass.indexOf('$');
		if(money != -1) {
			String prefix = srgClass.substring(0, money);
			String suffix = srgClass.substring(money);
			return repackage(prefix) + suffix;
		}
		
		//no packaging transformation, and it's not an inner class -> leave it alone
		return srgClass;
	}
}
