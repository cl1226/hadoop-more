package com.cnnc.sql

import java.util.Properties

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, IntegerType, StructField, StructType}

class MyAggFunc extends UserDefinedAggregateFunction {
  override def inputSchema: StructType = {
    StructType.apply(
      Seq(StructField("score", IntegerType))
    )
  }

  override def bufferSchema: StructType = {
    StructType.apply(
      Seq(StructField("sum", IntegerType), StructField("count", IntegerType))
    )
  }

  override def dataType: DataType = DoubleType

  override def deterministic: Boolean = true

  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0) = 0
    buffer(1) = 0
  }

  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0) = buffer.getInt(0) + input.getInt(0)
    buffer(1) = buffer.getInt(1) + 1
  }

  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) = buffer1.getInt(0) + buffer2.getInt(0)
    buffer1(1) = buffer1.getInt(1) + buffer2.getInt(1)
  }

  override def evaluate(buffer: Row) = {
    if (buffer.getInt(1) == 0) 1.toDouble else buffer.getInt(0).toDouble/buffer.getInt(1)
  }
}

object Demo {

  def main(args: Array[String]): Unit = {

    val session = SparkSession.builder()
      .appName("demo")
      .master("local")
//      .config("hive.metastore.uris", "thrift://node01:9083")
      .enableHiveSupport()
      .getOrCreate()
    val sc = session.sparkContext
    sc.setLogLevel("ERROR")

    import session.implicits._

    val data = List(
      ("a", "x", 50),
      ("b", "x", 30),
      ("c", "x", 90),
      ("a", "y", 80),
      ("b", "z", 60),
      ("c", "z", 70)
    ).toDF("name", "class", "score")

    data.createTempView("student")

    session.udf.register("myavg", new MyAggFunc)

    val res = session.sql("select name, " +
      " myavg(score)" +
      " from student " +
      " group by name")
    res.show()

    res.explain(true)




//    session.catalog.listDatabases().show()
//
//    session.sql("use default")
//
//    session.sql("create table test()")
//
//    session.catalog.listDatabases().show()

//    val properties = new Properties()
//    properties.put("url", "jdbc:mysql://localhost:3306/test")
//    properties.put("user", "root")
//    properties.put("password", "root")
//    properties.put("driver", "com.mysql.jdbc.Driver")
//
//    val df = session.read.jdbc(properties.getProperty("url"), "tbl_01", properties)
//    df.show()
//    df.createTempView("test_01")

//    session.sql("select * from test_01").show()

//    val dataset = session.read.textFile("spark/data/wc")
//
//
//    import session.implicits._
//
//    val map = dataset.flatMap(_.split(" ")).map((_, 1))
//
//    val frame = map.groupBy("_1").count()
//
//    frame.show()
  }

}
