import agency.highlysuspect.toybox.manifestindex.ManifestIndex
import agency.highlysuspect.toybox.manifestindex.GsonManifestIndexParser
import mill.*
import mill.define.Discover
import mill.testkit.{TestRootModule, UnitTester}
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
          println("downloading to " ++ d.toString())
          os.write(d, requests.get.stream(ManifestIndex.PISTON_META_URL))
          PathRef(d)
        }

        def doIt = Task.Anon {
          val parser = new GsonManifestIndexParser()
          parser.parseEntireIndex(fetchManifest().path.toNIO)
        }
      }

      val resourceFolder = os.Path(sys.env("MILL_TEST_RESOURCE_DIR"))
      UnitTester(build, resourceFolder).scoped { eval =>
        println(eval)

        val Right(result) = eval(build.doIt) : @unchecked
        val manifest = result.value
        assert("1.21.5".equals(manifest.latest.release))
      }
    }
  }
}