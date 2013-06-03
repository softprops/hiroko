package object hiroko {
  import dispatch.FunctionHandler
  import com.ning.http.client.Response
  // 2.10.+
  //implicit class ImplicitFunctionHandler(f: Response => T)
    //     extends FunctionHandler(f)
  implicit def r2h[T](f: Response => T): Client.Handler[T] =
    new FunctionHandler(f)
}
