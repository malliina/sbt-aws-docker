package com.malliina.sbt.aws

import sbt.File

import scala.language.implicitConversions

sealed trait YamlDoc extends YamlValue

sealed trait YamlValue {
  override def toString = Yaml.stringify(this)
}

case class YamlContainer(children: Seq[YamlDoc])

case class Section(title: String, parts: Seq[YamlValue]) extends YamlDoc

case class Arr(title: String, items: Seq[ArrEntry]) extends YamlDoc

case class TitledValue(title: String, value: Entry) extends YamlDoc

case class ArrEntry(value: String) extends YamlValue

case object EmptyRow extends YamlDoc

object ArrEntry {
  implicit def fromString(s: String): ArrEntry = ArrEntry(s)
}

case class Entry(value: String)

object Entry {
  implicit def fromString(s: String): Entry = Entry(s)

  implicit def fromFile(f: File): Entry = f.toString
}

object Yaml extends Yaml {
  val empty = ""
  val lineSep = "\n"
  val identStep = 2

  def stringifyDoc(yaml: YamlContainer): String =
    yaml.children.map(child => stringify(child)).mkString(empty, lineSep, lineSep)

  def stringify(yaml: YamlValue, ident: Int = 0): String = {
    val identation = spaces(ident)

    def stringifyParent(title: String, items: Seq[YamlValue]) = {
      val rows = items.map(item => stringify(item, ident + identStep)).mkString(lineSep)
      s"$title:$lineSep$rows"
    }

    val fragment = yaml match {
      case Section(title, parts) =>
        stringifyParent(title, parts)
      case Arr(title, items) =>
        stringifyParent(title, items)
      case TitledValue(title, value) =>
        s"$title: ${value.value}"
      case ArrEntry(value) =>
        s"- $value"
      case EmptyRow =>
        empty
    }
    s"$identation$fragment"
  }

  private def spaces(amount: Int) = Seq.fill(amount)(" ").mkString("")
}

trait Yaml {
  def doc(parts: YamlDoc*) = YamlContainer(parts)

  def section(title: String)(parts: YamlValue*) = Section(title, parts)

  def arr(title: String)(items: ArrEntry*) = Arr(title, items)

  def single(title: String, value: Entry) = TitledValue(title, value)

  def value(s: String) = Entry(s)

  def row = EmptyRow
}
