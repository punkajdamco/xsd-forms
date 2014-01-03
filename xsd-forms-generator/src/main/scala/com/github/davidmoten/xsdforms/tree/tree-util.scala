package com.github.davidmoten.xsdforms.tree

object TreeUtil {
  import xsd.Annotatedable
  import XsdUtil.AppInfoSchema

  def parseMakeVisibleMap(value: Option[String]): Map[String, Int] = {
    import Util._

    val Problem = "could not parse makeVisible, expecting 'value1->1,value2->2' (pairs delimited by comma and key value delimited by '->'"
    value match {
      case Some(s) =>
        s.split(",")
          .toList
          .map(
            x => {
              val items = x.split("->")
              if (items.length < 2)
                unexpected(Problem)
              (items(0), items(1).toInt)
            }).toMap
      case None => Map()
    }
  }

}

protected case class ElementWithNumber(element: ElementWrapper, number: Int)

//Use Vector because has O(1) append
protected case class HtmlJs(html: Vector[String], js: Vector[String]) {
  def addHtml(html2: String) = HtmlJs(html.+:(html2), js)
  def addJs(js2: String) = HtmlJs(html, js.+:(js2))
}

protected trait TreeState {
  import com.github.davidmoten.xsdforms.html.Html

  val options: Options

  val tree: Node

  implicit val idPrefix = options.idPrefix

  val html: Html

  //assign element numbers so that order of display on page 
  //will match order of element numbers. To do this must 
  //traverse children left to right before siblings
  private val elementNumbers = new ElementNumbersAssigner(tree).assignments

  implicit def toElementWithNumber(element: ElementWrapper): ElementWithNumber =
    ElementWithNumber(element, elementNumber(element))

  private def elementNumber(e: ElementWrapper): Int =
    elementNumbers.get(e).get;
}

  
