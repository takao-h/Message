import akka.stream.javadsl.Source
import scala.concurrent._
import akka._
import akka.actor._
import akka.stream._
import akka.stream.scaladsl._
import akka.util._
import scala.concurrent.Future

val s = Source.empty

val s1 = Source.single("single element")

//val s2 = Source(1 to 3)

//val s3 = Source(Future("single value from a Future"))

//s runForeach println

val s6 = Source.repeat(5)

//val source = Source(1 to 3)
//val sink = Sink.foreach[Int](elem => println(s"sink received: $elem"))
//val flow = source to sink
//flow.()

import java.util.concurrent.atomic.AtomicReference
import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.stream.scaladsl.{BroadcastHub, Flow, Keep, MergeHub, Sink}
import akka.stream.{KillSwitches, Materializer, UniqueKillSwitch}
import akka.stream.scaladsl.{Sink, Source}
import akka.NotUsed
import akka.io.Tcp.Message

import scala.collection.mutable.{Map => MutableMap}
import scala.concurrent.duration._
/**
  * Created by ruth on 2017/11/28.
  */

private def create(roomId: String): ChatRoom = {
  // MergeHubとBroadcastHubを使って、バスのパーツを作ります。
  val (sink, source) =
    MergeHub.source[ChatMessage](perProducerBufferSize = 16)
      .toMat(BroadcastHub.sink(bufferSize = 256))(Keep.both)
      .run()

  //排水口をつくります。
  source.runWith(Sink.ignore)

  val channel = ChatChannel(sink, source)

  // パーツを使って、チャットメッセージのバスをつくります。
  val bus: Flow[ChatMessage, ChatMessage, UniqueKillSwitch] = Flow.fromSinkAndSource(channel.sink, channel.source)
    .joinMat(KillSwitches.singleBidi[ChatMessage, ChatMessage])(Keep.right)
    .backpressureTimeout(3.seconds)
    .map { e =>
      println(s"$e $channel")
      e
    }
  ChatRoom(roomId, bus)
}



