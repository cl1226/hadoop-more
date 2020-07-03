package com.cnnc.test

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object Test {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("Demo")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")

    val ss = SparkSession.builder().config(conf).getOrCreate()
    import ss.implicits._

    val ds = ss.createDataset(sc.textFile("E:\\test.txt"))
    val df = ss.read.json(ds)

    df.createTempView("tbl")

    ss.sql("select sum(Horsepower) from tbl").show()
  }

}
