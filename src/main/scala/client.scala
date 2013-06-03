package hiroko

import com.ning.http.client.{ AsyncHandler, RequestBuilder }
import dispatch._, dispatch.Defaults._
import scala.concurrent.Future

object Client {
  val DefaultHeaders = Map("Accept" -> "application/json",
                           "User-Agent" -> "Hiroko/%s".format(
                             BuildInfo.version))
  type Handler[T] = AsyncHandler[T]
  trait Completion {
    def apply[T](handler: Client.Handler[T]): Future[T]
  }
}

/** a dispatch interface for https://api-docs.heroku.com/ */
case class Client(apikey: String, http: Http = Http)
  extends DefaultHosts
     with Methods {
   import Client._

  private [this] val credentials = BasicAuth(apikey)

  def request[T](req: RequestBuilder)(handler: Client.Handler[T]): Future[T] =
    http(credentials.sign(req) <:< DefaultHeaders > handler)

  def complete(req: RequestBuilder): Client.Completion =
    new Client.Completion {
      override def apply[T](handler: Client.Handler[T]) =
        request(req)(handler)
    }
}
