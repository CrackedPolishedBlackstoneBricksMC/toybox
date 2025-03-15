package agency.highlysuspect.toybox.manifestindex;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class closely mirrors the version_manifest_v2 format
 */
public class ManifestIndex {
	public List<VersionData> versions;
	public Latest latest;
	
	public static class Latest {
		public String release;
		public String snapshot;
	}
	
	public static class VersionData {
		public String id;
		public String type;
		public String url;
		public Instant time;
		public Instant releaseTime;
		public String sha1;
		public int complianceLevel;
	}
	
	/// Conveniences ///
	
	public static String PISTON_META_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json";
	
	protected transient Map<String, VersionData> versionMap = null;
	
	public ManifestIndex useVersionMapCache() {
		if(versionMap == null) {
			Map<String, VersionData> map = new LinkedHashMap<>();
			for(VersionData vd : this.versions) map.put(vd.id, vd);
			versionMap = map;
		}
		
		return this;
	}
	
	public ManifestIndex dropVersionMapCache() {
		versionMap = null;
		return this;
	}
	
	public Map<String, VersionData> getOrCreateVersionMapCache() {
		useVersionMapCache();
		return versionMap;
	}
	
	public VersionData get(String ver) {
		if(versionMap == null) {
			//Linear search
			for(VersionData vd : this.versions) if(vd.id.equals(ver)) return vd;
			return null;
		} else {
			//Map lookup
			return versionMap.get(ver);
		}
	}
	
	public VersionData getLatestRelease() {
		return get(latest.release);
	}
	
	public VersionData getLatestSnapshot() {
		return get(latest.snapshot);
	}
}
