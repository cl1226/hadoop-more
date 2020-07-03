package com.cnnc

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * {"Name":"chevrolet chevelle malibu", "Miles_per_Gallon":18, "Cylinders":8, "Displacement":307, "Horsepower":130, "Weight_in_lbs":3504, "Acceleration":12, "Year":"1970-01-01", "Origin":"USA"}
 * {"Name":"buick skylark 320", "Miles_per_Gallon":15, "Cylinders":8, "Displacement":350, "Horsepower":165, "Weight_in_lbs":3693, "Acceleration":11.5, "Year":"1970-01-01", "Origin":"USA"}
 * {"Name":"plymouth satellite", "Miles_per_Gallon":18, "Cylinders":8, "Displacement":318, "Horsepower":150, "Weight_in_lbs":3436, "Acceleration":11, "Year":"1970-01-01", "Origin":"USA"}
 */
object Demo {


  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Demo").setMaster("local[*]")

    val sc = new SparkContext(conf)

    val session = SparkSession.builder().appName("RddToDataFrame")
      .master("local[*]").getOrCreate()

    sc.setLogLevel("ERROR")

    import session.implicits._

    val df = sc.parallelize(List(
      ("cookie1", "2019/05/14", "1"),
      ("cookie1", "2019/05/15", "2"),
      ("cookie1", "2019/05/16", "3"),
      ("cookie1", "2019/05/17", "4"),
      ("cookie2", "2019/05/14", "2"),
      ("cookie2", "2019/05/15", "3"),
      ("cookie2", "2019/05/16", "1"),
      ("cookie2", "2019/05/17", "4"),
      ("cookie3", "2019/05/14", "5"),
      ("cookie3", "2019/05/15", "1"),
      ("cookie3", "2019/05/16", "2")
    )).toDF("cookie_id", "create_time", "pv")

    df.createTempView("tbl")

//    session.sql("select * from tbl").show()

//    session.sql("select *," +
//      " sum(a.pv) over(partition by a.cookie_id order by a.create_time rows between 1 preceding and 1 following) as pv1" +
//      " from tbl a").show()

    session.sql("select *," +
      " first_value(pv) over(partition by a.cookie_id order by a.create_time) as pv1," +
      " last_value(pv) over(partition by a.cookie_id order by a.create_time) as pv2" +
      " from tbl a").show()

    session.close()
  }

}
