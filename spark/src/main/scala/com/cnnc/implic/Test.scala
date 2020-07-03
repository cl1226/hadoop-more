package com.cnnc.implic

import java.io.File

import scala.io.Source

class RichFile(val file: File) {
  def read = {
    Source.fromFile(file.getPath).mkString
  }
}

object Context {
  implicit def file2RichFile(file: File) = new RichFile(file)
}

object Test {

  def person(implicit name: Int) = name

  implicit val p = "aaa"

  implicit val q = 123

  def print(msg: String) = println(msg)

  implicit def int2Str(x: Int) = x.toString

  def main(args: Array[String]): Unit = {
    val v = person
    println(v)

    println("-----------")

    print(222)

    import Context.file2RichFile

    println(new File("E:\\test.txt").read)
  }


}
