import agency.highlysuspect.toybox.manifestindex.ManifestIndex
import agency.highlysuspect.toybox.manifestindex.gson.GsonManifestIndexParser
import mill._
import mill.testkit.{TestBaseModule, UnitTester}
import utest._;

object NorthstarTests extends TestSuite {
  override def tests: Tests = Tests {
    test("unit") {
      object build extends TestBaseModule {
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

        val Right(manifest) = eval(build.doIt);
        println(manifest)

        assert(true)
      }
    }
  }
}