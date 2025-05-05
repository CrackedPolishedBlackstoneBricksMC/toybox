import agency.highlysuspect.toybox.manifestindex.ManifestIndexParser;
import agency.highlysuspect.toybox.manifestindex.gson.GsonManifestIndexParser;

public class ToyboxGsonManifestIndexParserTests extends AbstractToyboxManifestIndexParserTests {
	@Override
	protected ManifestIndexParser getParser() {
		return new GsonManifestIndexParser();
	}
}
