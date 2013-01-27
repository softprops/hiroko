package hiroko

import dispatch._
import com.ning.http.client.{ RequestBuilder, Response }

trait Credentials {
  def sign(req: RequestBuilder): RequestBuilder
}

case class BasicAuth(key: String) extends Credentials {
  override def sign(req: RequestBuilder) =
    req.as_!("", key)
}
