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