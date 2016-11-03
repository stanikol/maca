package example

import com.thoughtworks.binding.Binding.{Constants, Var, Vars}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.{Event, document, window}
import org.scalajs.dom.raw.{HTMLInputElement, Node, HTMLDivElement}
import org.scalajs.dom.ext.Ajax

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import org.scalajs.jquery.jQuery

//import io.udash.wrappers.jquery._
import upickle.default._
import shared.Model._

object MacaAdmin extends js.JSApp {



  def main(): Unit = {
    println("Hello")
  }
}
