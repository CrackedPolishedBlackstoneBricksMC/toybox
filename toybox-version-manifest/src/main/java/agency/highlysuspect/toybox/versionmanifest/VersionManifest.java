package agency.highlysuspect.toybox.versionmanifest;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class VersionManifest {
	//TODO: arguments
	public AssetIndexReference assetIndex;
	public String assets;
	public int complianceLevel;
	public Map<String, Download> downloads;
	public String id;
	public JavaVersion javaVersion;
	public List<Library> libraries;
	//TODO: logging
	public String mainClass;
	public int minimumLauncherVersion;
	public Instant releaseTime;
	public Instant time;
	public String type;
	
	public static class AssetIndexReference {
		public String id;
		public String sha1;
		public int size;
		public int totalSize;
		public String url;
	}
	
	public static class Download {
		public String sha1;
		public int size;
		public String url;
	}
	
	public static class JavaVersion {
		public String component;
		public int majorVersion;
	}
	
	public static class Library {
		public LibraryDownloads downloads;
		public String name;
		public List<LibraryRule> rules;
		
		//TODO: natives
		
		//Used by forge 1.6/1.7 internal version.json
		//Not vanilla
		@SerializedName("url")
		public String forgeDownloadRoot;
	}
	
	public static class LibraryDownloads {
		public LibraryArtifact artifact;
		public Map<String, LibraryArtifact> classifiers;
	}
	
	public static class LibraryArtifact {
		public String path;
		public String sha1;
		public int size;
		public String url;
	}
	
	//TODO: missing version bounds
	public static class LibraryRule {
		public String action;
		public OS os;
		
		public static class OS {
			public String name;
			public String version;
		}
	}
}
