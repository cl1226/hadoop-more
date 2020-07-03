package com.cnnc.pi

import scala.math.random
import org.apache.spark.{SparkConf, SparkContext}

object Demo {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("Spark PI")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")

    val slices = if(args.length > 0) args(0).toInt else 100
    val n = math.min(100000L * slices, Int.MaxValue).toInt
    val count = sc.parallelize(1 until n, slices).map(i => {
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x * x + y * y <= 1) 1 else 0
    }).reduce(_ + _)
    println(4.0*count / (n-1))

    Thread.sleep(Long.MaxValue)
  }

}
