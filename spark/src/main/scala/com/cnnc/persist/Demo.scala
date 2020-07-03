package com.cnnc.persist

import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object Demo {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Demo").setMaster("local")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")

    sc.setCheckpointDir("spark/data/checkpoint")

    val rdd = sc.parallelize(1 to 10)

    val d2rdd = rdd.map(e => if (e % 2 == 0) ("A", e) else ("B", e))

    d2rdd.persist(StorageLevel.MEMORY_AND_DISK_SER)
    d2rdd.checkpoint()

    val res01 = d2rdd.map(x => (x._1, 1))
    res01.reduceByKey(_+_).foreach(println)

    Thread.sleep(9999999)
  }

}
