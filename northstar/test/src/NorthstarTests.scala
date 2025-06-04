import mill.*
import mill.define.Discover
import mill.testkit.{TestRootModule, UnitTester}
import agency.highlysuspect.toybox.download.*
import agency.highlysuspect.toybox.download.mill.*
import agency.highlysuspect.toybox.manifestindex.ManifestIndex
import agency.highlysuspect.toybox.manifestindex.GsonManifestIndexParser
import utest.*

object NorthstarTests extends TestSuite {
  override def tests: Tests = Tests {
    test("unit") {
      object build extends TestRootModule {
        //mill 1.0 test boilerplate(?)
        //ok yeah it looks like this is some special compile-time-metadata sauce
        //and normally you don't see it since mill's preprocessor does it
        override lazy val millDiscover = Discover[this.type]

        def fetchManifest = Task[PathRef] {
          val d = Task.dest / "version_manifest_v2.json"
          
          val spec: DownloadSpec = new DownloadSpec(ManifestIndex.PISTON_META_URL)
          val dest: PathDownloadDest = new PathDownloadDest(d.toNIO)
          val backend: DownloadBackend = RequestsBackend()
          backend.newDownloader(spec, dest).download()

          PathRef(d)
        }

        def doIt = Task.Anon {
          val parser = new GsonManifestIndexParser()
          parser.parseEntireIndex(fetchManifest().path.toNIO)
        }
      }

      val resourceFolder = os.Path(sys.env("MILL_TEST_RESOURCE_DIR"))
      UnitTester(build, resourceFolder).scoped { eval =>
        val Right(result) = eval(build.doIt) : @unchecked
        val manifest = result.value
        assert("1.21.5".equals(manifest.latest.release))
      }
    }
  }
}