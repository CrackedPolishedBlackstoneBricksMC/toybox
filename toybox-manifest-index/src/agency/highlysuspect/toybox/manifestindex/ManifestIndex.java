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
	
	public VersionData getLinearSearch(String ver) {
		for(VersionData vd : this.versions) if(vd.id.equals(ver)) return vd;
		return null;
	}
	
	public ManifestIndexMap toMap() {
		return new ManifestIndexMap(this);
	}
}
