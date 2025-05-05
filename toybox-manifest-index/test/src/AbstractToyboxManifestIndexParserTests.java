import agency.highlysuspect.toybox.manifestindex.ManifestIndex;
import agency.highlysuspect.toybox.manifestindex.ManifestIndexMap;
import agency.highlysuspect.toybox.manifestindex.ManifestIndexParser;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractToyboxManifestIndexParserTests {
	protected abstract ManifestIndexParser getParser();
	
	//As of the snapshot taken in vmv2In()
	protected static final String LATEST_RELE = "1.21.5";
	protected static final String LATEST_SNAP = "25w18a";
	
	protected InputStream vmv2In() {
		return AbstractToyboxManifestIndexParserTests.class.getClassLoader()
			.getResourceAsStream("version_manifest_v2_as_of_may_4_2025.json");
	}
	
	protected void testvmv2(ManifestIndex mi) {
		//latest version is correct and defined
		assertEquals(LATEST_RELE, mi.latest.release);
		assertEquals(LATEST_SNAP, mi.latest.snapshot);
		
		//getLinearSearch == getMap
		ManifestIndexMap imap = mi.toMap();
		
		ManifestIndex.VersionData twelveLinear = mi.getLinearSearch("1.12.2");
		assertNotNull(twelveLinear);
		assertSame(twelveLinear, imap.get("1.12.2"));
		
		ManifestIndex.VersionData latestRele = mi.getLinearSearch(LATEST_RELE);
		assertNotNull(latestRele);
		assertSame(latestRele, imap.getLatestRelease());
		
		ManifestIndex.VersionData latestSnap = mi.getLinearSearch(LATEST_SNAP);
		assertNotNull(latestSnap);
		assertSame(latestSnap, imap.getLatestSnapshot());
		
		//version looks ok
		assertIsOneTwelveTwo(twelveLinear);
	}
	
	protected void assertIsOneTwelveTwo(ManifestIndex.VersionData vdLinear) {
		//{"id": "1.12.2", "type": "release", "url": "https://piston-meta.mojang.com/v1/packages/832d95b9f40699d4961394dcf6cf549e65f15dc5/1.12.2.json", "time": "2023-06-07T11:49:20+00:00", "releaseTime": "2017-09-18T08:39:46+00:00", "sha1": "832d95b9f40699d4961394dcf6cf549e65f15dc5", "complianceLevel": 0}
		assertEquals("1.12.2", vdLinear.id);
		assertEquals("release", vdLinear.type);
		assertEquals("https://piston-meta.mojang.com/v1/packages/832d95b9f40699d4961394dcf6cf549e65f15dc5/1.12.2.json", vdLinear.url);
		assertEquals(OffsetDateTime.of(2023, 6, 7, 11, 49, 20, 0, ZoneOffset.UTC), vdLinear.time);
		assertEquals(OffsetDateTime.of(2017, 9, 18, 8, 39, 46, 0, ZoneOffset.UTC), vdLinear.releaseTime);
		assertEquals("832d95b9f40699d4961394dcf6cf549e65f15dc5", vdLinear.sha1);
		assertEquals(0, vdLinear.complianceLevel);
	}
	
	@Test
	public void stringParser() throws Exception {
		try(InputStream in = vmv2In()) {
			testvmv2(getParser().parseEntireIndex(readToString(in)));
		}
	}
	
	@Test
	public void readerParser() throws Exception {
		try(InputStreamReader isn = new InputStreamReader(vmv2In())) {
			testvmv2(getParser().parseEntireIndex(isn));
		}
	}
	
	@Test
	public void incrementalParser() throws Exception {
		//ground truth
		ManifestIndexMap map;
		try(InputStreamReader isn = new InputStreamReader(vmv2In())) {
			map = getParser().parseEntireIndex(isn).toMap();
		}
		
		//parsing a specific version
		try(InputStreamReader isn = new InputStreamReader(vmv2In())) {
			ManifestIndex.VersionData vdInc = getParser().parseOneVersion(isn, "1.12.2");
			assertEquals(map.get("1.12.2"), vdInc);
			assertIsOneTwelveTwo(vdInc);
		}
		
		//parsing latest release and snap
		try(InputStreamReader isn = new InputStreamReader(vmv2In())) {
			ManifestIndex.VersionData latestRelease = getParser().parseLatestRelease(isn);
			assertEquals(map.getLatestRelease(), latestRelease);
		}
		try(InputStreamReader isn = new InputStreamReader(vmv2In())) {
			ManifestIndex.VersionData latestSnap = getParser().parseLatestSnapshot(isn);
			assertEquals(map.getLatestSnapshot(), latestSnap);
		}
	}
	
	//todo put this somewhere handy
	private String readToString(InputStream in) throws IOException {
		if(in == null) throw new IllegalArgumentException("null input stream");
		ByteArrayOutputStream bob = new ByteArrayOutputStream();
		byte[] shuttle = new byte[4096];
		int read;
		while((read = in.read(shuttle)) != -1) bob.write(shuttle, 0, read);
		return new String(bob.toByteArray(), StandardCharsets.UTF_8);
	}
}
