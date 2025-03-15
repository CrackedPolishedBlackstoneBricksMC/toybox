package agency.highlysuspect.toybox.mapping.mcp;

import agency.highlysuspect.toybox.mapping.MapRepackager;
import agency.highlysuspect.toybox.mapping.Repackager;
import agency.highlysuspect.toybox.string.StringInterner;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PackagesCsvParser {
	protected final Map<String, String> packages = new HashMap<>();
	
	@Contract("_, _ -> this")
	public PackagesCsvParser read(Path path, StringInterner mem) throws IOException {
		return read(Files.readAllLines(path), mem);
	}
	
	@Contract("_, _ -> this")
	public PackagesCsvParser read(Iterable<String> lines, StringInterner mem) {
		int lineNo = 0;
		for(String lineUntrim : lines) {
			lineNo++;
			String line = lineUntrim.trim();
			
			//skip blanks
			if(line.isEmpty()) continue;
			
			//skip CSV header
			if(lineNo == 1 && "class,package".equals(line)) continue;
			
			String[] split = mem.internArray(line.split(",", 2));
			if(split.length != 2) throw new IllegalStateException("line " + lineNo + " has weird number of elements: " + line);
			
			packages.put(split[0], split[1]);
		}
		
		return this;
	}
	
	public Map<String, String> getPackages() {
		return packages;
	}
	
	public Repackager toRepackager() {
		return new MapRepackager(packages);
	}
}
