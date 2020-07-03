package com.cnnc.wordcount

import org.apache.spark.{SparkConf, SparkContext}

object Demo {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[2]").setAppName("WC")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")

    val file = sc.textFile("spark/data/wc")
//
//    val flatMap = file.flatMap(_.split(" "))
//
//    val map = flatMap.map((_, 1))
//
//    val result = map.reduceByKey(_ + _)
//
//    result.foreach(println)

    val data = sc.parallelize(Seq((3, 2), (2, 3), (1, 1), (4, 3)))

    data.repartition(1).sortByKey(false).sortBy(_._2, false).foreach(println)

    data.foreach(println)

//    data.sortByKey(false).sortBy(_._2, false).foreach(println)
//    data.map(x => (1, x._2)).sortBy(_._2, false).foreach(println)
//    data.sortBy(_._2, false).foreach(println)

    while (true){}
  }

}
