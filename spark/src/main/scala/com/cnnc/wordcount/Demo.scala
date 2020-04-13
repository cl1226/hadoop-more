package com.cnnc.wordcount

import org.apache.spark.{SparkConf, SparkContext}

object Demo {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("WC")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")

    val file = sc.textFile("spark/data/wc")

    val flatMap = file.flatMap(_.split(" "))

    val map = flatMap.map((_, 1))

    val result = map.reduceByKey(_ + _)

    result.foreach(println)
  }

}
