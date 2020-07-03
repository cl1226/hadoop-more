package com.cnnc.sql

import org.apache.spark.sql.SparkSession

object SparkSQLJoinDemo {

  def main(args: Array[String]): Unit = {
    val ss = SparkSession.builder().appName("JoinTest").master("local[*]").getOrCreate()

    ss.sparkContext.setLogLevel("ERROR")

    import ss.implicits._

//    ss.conf.set("spark.sql.autoBroadcastJoinThreshold", -1)
    ss.conf.set("spark.sql.join.preferSortMergeJoin", true)

    println(ss.conf.get("spark.sql.autoBroadcastJoinThreshold"))

    val df1 = Seq(("0", "a"), ("1", "b"), ("2", "c")).toDF("id", "name")

    val df2 = Seq(("0", "d"), ("1", "e"), ("2", "f")).toDF("aid", "aname")

    df2.repartition()

    val result = df1.join(df2, $"id" === $"aid")

//    result.explain()

    result.show()

    while(true) {}
  }

}
