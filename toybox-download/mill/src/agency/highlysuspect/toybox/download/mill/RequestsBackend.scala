package agency.highlysuspect.toybox.download.mill

import agency.highlysuspect.toybox.download.{DownloadBackend, DownloadDest, DownloadSpec}
import requests.{Session, Compress}
import scala.util.Using

case class RequestsBackend(session: Session = Session()) extends DownloadBackend {
  override def download(spec: DownloadSpec, dest: DownloadDest): Unit = {
    Using(dest.makeOutputStream()) { out =>

      val compress = if(spec.requestGzip) { Compress.Gzip } else { Compress.None }

      //TODO TODO TODO: handle etags!!!
      // basically if the etag matches i don't need to download the rest of the file
      // but im not sure i can get at the headers with this fancy api though

      val readable = session.get.stream(url = spec.url, compress = compress, autoDecompress = true, onHeadersReceived = headers => {
        //something something etag??
      })
      readable.writeBytesTo(out)
    }
  }
}
