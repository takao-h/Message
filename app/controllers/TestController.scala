import javax.inject.Inject

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.libs.streams._
import play.api.libs.json.JsValue
import akka.actor._

class TestController @Inject()(implicit system: ActorSystem, materializer: Materializer) {


  class MyWebSocketActor(out: ActorRef) extends Actor {


    def receive = {
      case msg: JsValue =>
        out ! msg
    }
  }

  object MyWebSocketActor {
    def props(out: ActorRef) = Props(new MyWebSocketActor(out))
  }

  def socket = WebSocket.accept[JsValue, JsValue] { request =>
    ActorFlow.actorRef(out => MyWebSocketActor.props(out))
  }

}