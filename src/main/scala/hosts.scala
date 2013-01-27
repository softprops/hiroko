package hiroko

import dispatch._
import com.ning.http.client.RequestBuilder

trait Hosts {
  def api: RequestBuilder
}

trait DefaultHosts extends Hosts {
  override def api = :/("api.heroku.com").secure
}

