package com.cnnc.test

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

object Demo {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Demo").setMaster("local[*]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")

    val data = sc.textFile("E:\\test.txt")
    val res = data.map(x => {
      val s = x.split(",")(4)
      val value = s.split(":")(1)
      value.toInt
    }).reduce(_ + _)

    println(res)
  }

}
