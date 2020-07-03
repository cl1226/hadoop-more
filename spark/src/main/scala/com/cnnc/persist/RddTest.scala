package com.cnnc.persist

import java.util.Date

import org.apache.spark.HashPartitioner
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object RddTest extends App {

  val spark = SparkSession.builder()
    .appName("RddTest")
    .master("local[*]")
    .getOrCreate()

  spark.sparkContext.setCheckpointDir("./checkpoint")

  //  spark.sparkContext.setLogLevel("error")

  val rdd = spark.sparkContext.makeRDD(Array(("a", 1), ("a", 2), ("b", 5), ("b", 6), ("b", 7), ("b", 8)
    , ("a", 3), ("a", 4), ("b", 7), ("b", 8), ("b", 7), ("b", 8), ("b", 7), ("b", 8), ("b", 7), ("b", 8)), 4)
  val partitioner = new HashPartitioner(2)
  rdd.partitionBy(partitioner)

  val result = rdd //.mapPartitions(items => items.map(item => (item._1, item._2 * 10)))
    //    .coalesce(1)
    .combineByKey(
      (c: Int) => 1
      , (u: Int, v: Int) => {
        u + 1
      }
      , (u1: Int, u2: Int) => {
        println(new Date())
        u1 + u2
      }
      , partitioner
    ) //.cache()

  //  println(result.dependencies)
  //  result.checkpoint()
  //    .reduce(_ + _)
  //  result.foreach(println)

  case class Planet(f1: String, f2: String, f3: String)

  val rdd1 = spark.sparkContext.makeRDD(Array(("a1", "b1", "c1:c2:c3"),("a2", "b2", "c1:c2:c3")), 2)
  val rdd2 = rdd1.flatMap(item => {
    val cList = item._3.split(":")
    var arr = List[Planet]()
    for (x <- cList) {
      arr = arr :+ Planet(item._1, item._2, x)
    }
    arr
  })
  rdd2.checkpoint()
  rdd2.collect().foreach(x => {
    println(x.f1, x.f2, x.f3)
  })

  spark.sparkContext.makeRDD(List(("a1","b1")))
//      .flatMapValues()


  //  println("**************")
  //  result.foreach(println)

  //  println(result)

  Thread.sleep(1000 * 60 * 60)
  spark.stop()
}

