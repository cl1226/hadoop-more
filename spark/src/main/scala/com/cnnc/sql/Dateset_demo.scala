package com.cnnc.sql

import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object Dateset_demo {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("demo")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")

    val data = sc.parallelize(1 to 10, 2)

    val res = data.aggregate(1)(_ + _, _ + _)

    println(res)

//    val rdd = data.map((_, 1)).partitionBy(new HashPartitioner(1))
//
//    val reduce01 = rdd.reduceByKey(_ + _)
//
//    val map02 = reduce01.filter(_._1>5)
////    val map02 = reduce01.map((_, 2)).partitionBy(new HashPartitioner(1))
//
//    map02.reduceByKey(_+_).foreach(println)
//
//    while (true) {}

  }

}
