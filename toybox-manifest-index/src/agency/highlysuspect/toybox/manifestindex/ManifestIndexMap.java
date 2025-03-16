package agency.highlysuspect.toybox.manifestindex;

import java.util.LinkedHashMap;

public class ManifestIndexMap extends LinkedHashMap<String, ManifestIndex.VersionData> {
	public ManifestIndexMap(ManifestIndex index) {
		super((int) (index.versions.size() * 1.5)); //load-factor bullshit
		
		for(ManifestIndex.VersionData vd : index.versions) put(vd.id, vd);
		this.latest = index.latest;
	}
	
	protected final ManifestIndex.Latest latest;
	
	public ManifestIndex.VersionData getLatestRelease() {
		return get(latest.release);
	}
	
	public ManifestIndex.VersionData getLatestSnapshot() {
		return get(latest.snapshot);
	}
}
