package com.cnnc.topn

import org.apache.spark.{SparkConf, SparkContext}

object Demo {

  val conf = new SparkConf().setMaster("local").setAppName("TopN")
  val sc = new SparkContext(conf)
  sc.setLogLevel("ERROR")

  // 2019-6-1	39
  val file = sc.textFile("spark/data/weather")
  val mapRDD = file.map(x => {
    (x.split("\t")(0), x.split("\t")(1))
  }).map(x => {
    (x._1.split("-")(0),
      x._1.split("-")(1),
      x._1.split("-")(2),
      x._2)
  })
  mapRDD.foreach(println)
}
