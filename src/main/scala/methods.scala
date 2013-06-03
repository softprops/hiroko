package hiroko

import dispatch._
import com.ning.http.client.RequestBuilder
import org.json4s.JsonDSL._
import org.json4s.native.Printer.compact
import org.json4s.native.JsonMethods.render

trait Methods { self: Client =>

  object Apps extends Client.Completion {
    private [this] def base = api / "apps"
    protected [this] case class App(name: String)
      extends Client.Completion {

      protected [this]
      case class LogBuilder(
        app: String,
        _lines: Int = 20,
        _ps: Option[String] = None,
        _src: Option[String] = None,
        _tail: Option[Int] = None)
        extends Client.Completion {
        def lines(n: Int) = copy(_lines = n)
        def ps(name: String) = copy(_ps = Some(name))
        def source(src: String) = copy(_src = Some(src))
        def tail = copy(_tail = Some(1))
        private def pmap =
          Map("logplex" -> "true",
             "num" -> _lines.toString) ++
              _tail.map("tail" -> _.toString)
              _ps.map("ps" -> _) ++
              _src.map("source" -> _)

        /** @return a url for accessing application logs */
        override def apply[T](handler: Client.Handler[T]) =
          request(api / "apps" / app / "logs" <<? pmap)(handler)
      }

      protected [this] object Config
      extends Client.Completion {
        private [this] def base =
          api / "apps" / name / "config_vars"

        /** https://api-docs.heroku.com/config#GET/apps/config_vars */
        override def apply[T](hand: Client.Handler[T]) =
          request(base)(hand)

        /** https://api-docs.heroku.com/config#PUT/apps/config_vars */
        def set(vars: (String, String)*) = {
          var map = vars.toMap
          complete(base.PUT.setBody(compact(render(map))))
        }

        /** https://api-docs.heroku.com/config#DELETE/apps/config_vars */
        def delete(key: String) =
          complete(base.DELETE / key)
      }

      protected [this] object Collaborators
      extends Client.Completion {
        private [this] def base =
          api / "apps" / name / "collaborators"

        /** https://api-docs.heroku.com/collaborators#GET/apps/collaborators */
        override def apply[T](hand: Client.Handler[T]) =
          request(base)(hand)

        /** https://api-docs.heroku.com/collaborators#POST/apps/collaborators */
        def add(email: String) =
          complete(base.POST << Map("collaborator[email]" -> email))

        /** https://api-docs.heroku.com/collaborators#DELETE/apps/collaborators */
        def remove(email: String) =
          complete(base.DELETE / email)
      }
  
      protected [this] object Addons
      extends Client.Completion {
        private [this] def base =
          api / "apps" / name / "addons"

        /** https://api-docs.heroku.com/addons#GET/apps/addons */
        override def apply[T](hand: Client.Handler[T]) =
          request(base)(hand)

        /** https://api-docs.heroku.com/addons#POST/apps/addons */
        def install(addon: String) =
          complete(base.POST / addon)

        /** https://api-docs.heroku.com/addons#PUT/apps/addons */
        def upgrade(addon: String) =
          complete(base.PUT / addon)

        /** https://api-docs.heroku.com/addons#DELETE/apps/addons */
        def uninstall(addon: String) =
          complete(base.DELETE / addon)
      }

      protected [this] object Domains
      extends Client.Completion {
        private [this] def base = 
          api / "apps" / name / "domains"

        /** https://api-docs.heroku.com/domains#GET/apps/domains */
        override def apply[T](hand: Client.Handler[T]) = 
          request(base)(hand)

        /** https://api-docs.heroku.com/domains#POST/apps/domains */
        def add(domain: String) =
          complete(base.POST << Map("domain_name[domain]" -> domain))

        /** https://api-docs.heroku.com/domains#DELETE/apps/domains */
        def remove(domain: String) =
          complete(base.DELETE / domain)
      }

      protected [this] object Releases
      extends Client.Completion {
        private [this] def base =
          api / "apps" / name / "releases"

        /** https://api-docs.heroku.com/releases#GET/apps/releases */
        override def apply[T](hand: Client.Handler[T]) =
          request(base)(hand)

        /** https://api-docs.heroku.com/releases#GET/apps/releases */
        def apply(release: String) =
          complete(base / release)

        /** https://api-docs.heroku.com/releases#POST/apps/releases */
        def rollback(release: String) =
          complete(base.POST << Map("rollback" -> release))
      }

      protected [this] object Processes
      extends Client.Completion {
        private [this] def base = 
          api / "apps" / name / "ps"

        /** https://api-docs.heroku.com/ps#GET/apps/ps */
        override def apply[T](hand: Client.Handler[T]) =
          request(base)(hand)

        /** https://api-docs.heroku.com/ps#POST/apps/ps*/
        def run(cmd: String) =  // todo support `attach`
          complete(base.POST << Map("commmand" -> cmd))

        /** https://api-docs.heroku.com/ps#POST/apps/ps/restart */
        def restart =
          complete(base.POST / "restart")

        /** https://api-docs.heroku.com/ps#POST/apps/ps/stop */
        def stop =
          complete(base.POST / "stop")

        /** https://api-docs.heroku.com/ps#POST/apps/ps/scale */
        def scale(typ: String, n: Int) =
          complete(base.POST / "scale" << Map("type" -> typ, "qty" -> n.toString))
      }
      
      protected [this] object Stacks
      extends Client.Completion {
        private [this] def base =
          api / "apps" / name / "stack"
        
        /** https://api-docs.heroku.com/stacks#GET/apps/stack */
        override def apply[T](hand: Client.Handler[T]) =
          request(base)(hand)

        /** https://api-docs.heroku.com/stacks#PUT/apps/stack */
        def migrate(stack: String) =
          complete((base << stack).PUT)
      }

      /** https://api-docs.heroku.com/apps#GET/apps */
      override def apply[T](hand: Client.Handler[T]) =
        request(base / name)(hand)

      /** https://api-docs.heroku.com/apps#PUT/apps */
      def rename(newname: String) =
        complete((base / name << Map("app[name]" -> newname)).PUT)

      /** https://api-docs.heroku.com/apps#PUT/apps */
      def transfer(owner: String) =
        complete((base / name << Map("app[transfer_owner]" -> owner)).PUT)

      /** https://api-docs.heroku.com/apps#DELETE/apps */
      def destroy =
        complete(base.DELETE / name)

      /** https://api-docs.heroku.com/apps#POST/apps/server/maintenance */
      def maintenance(on: Boolean) =
        complete(base.POST / name / "server" / "maintenance"<< Map(
          "maintenance_mode" -> (if (on) "1" else "0")))

      def config = Config

      def collaborators = Collaborators

      def addons = Addons

      def domains = Domains

      def releases = Releases

      /** https://api-docs.heroku.com/logs#GET/apps/logs */
      def logs = LogBuilder(name)

      def ps = Processes

      def stacks = Stacks
    }

    /** https://api-docs.heroku.com/apps#GET/apps */
    override def apply[T](hand: Client.Handler[T]) =
      request(base)(hand)
    
    def apply(name: String) = App(name)

    protected [this] case class AppCreator(nameval: Option[String] = None,
                                           stackval: Option[String] = None)
       extends Client.Completion {
      def name(n: String) = copy(nameval = Some(n))
      def stack(s: String) = copy(stackval = Some(s))
      override def apply[T](hand: Client.Handler[T]) =
        request(base.POST << pmap)(hand)
      private def pmap = Map.empty[String, String] ++
                           nameval.map("app[name]" -> _) ++
                           stackval.map("app[stack]" -> _)
    }
    /** https://api-docs.heroku.com/apps#POST/apps */
    def create = AppCreator()
  }

  protected [this] object Keys
  extends Client.Completion {
    private [this] def base = api / "user" / "keys"

    /** https://api-docs.heroku.com/keys#GET/user/keys */
    override def apply[T](hand: Client.Handler[T]) =
      request(base)(hand)

    /** https://api-docs.heroku.com/keys#POST/user/keys */
    def add(key: String) =
      complete(base.POST << key)

    /** https://api-docs.heroku.com/keys#DELETE/user/keys */
    def remove(userathost: String) =
      complete(base.DELETE / userathost)

    /** https://api-docs.heroku.com/keys#DELETE/user/keys */
    def clear =
      complete(base.DELETE)
  }

  def apps = Apps

  def app(name: String) = Apps(name)

  /** https://api-docs.heroku.com/addons#GET/addons */
  def addons = complete(api / "addons")

  def keys = Keys
}
