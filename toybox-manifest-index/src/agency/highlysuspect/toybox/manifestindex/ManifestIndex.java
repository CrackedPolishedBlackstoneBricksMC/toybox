package agency.highlysuspect.toybox.manifestindex;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class closely mirrors the version_manifest_v2 format
 */
public class ManifestIndex {
	public List<VersionData> versions;
	public Latest latest;
	
	public static class Latest {
		public String release;
		public String snapshot;
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			Latest latest = (Latest) o;
			return Objects.equals(release, latest.release) && Objects.equals(snapshot, latest.snapshot);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(release, snapshot);
		}
	}
	
	public static class VersionData {
		public String id;
		public String type;
		public String url;
		public OffsetDateTime time;
		public OffsetDateTime releaseTime;
		public String sha1;
		public int complianceLevel;
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			VersionData that = (VersionData) o;
			return complianceLevel == that.complianceLevel && Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(url, that.url) && Objects.equals(time, that.time) && Objects.equals(releaseTime, that.releaseTime) && Objects.equals(sha1, that.sha1);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(id, type, url, time, releaseTime, sha1, complianceLevel);
		}
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
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		ManifestIndex that = (ManifestIndex) o;
		return Objects.equals(versions, that.versions) && Objects.equals(latest, that.latest);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(versions, latest);
	}
}
