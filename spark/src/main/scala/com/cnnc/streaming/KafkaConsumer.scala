package com.cnnc.streaming

import java.time.Duration
import java.util
import java.util.Properties
import java.util.regex.Pattern

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRebalanceListener, KafkaConsumer}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer

object KafkaConsumer {

  def main(args: Array[String]): Unit = {

    val prop = new Properties()
    prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.41.193:9092")
    prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])

    prop.put(ConsumerConfig.GROUP_ID_CONFIG, "G1")

    //earliest latest
    prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")


    val consumer = new KafkaConsumer[String, String](prop)

    consumer.subscribe(Pattern.compile("topic02"), new ConsumerRebalanceListener {
      override def onPartitionsRevoked(collection: util.Collection[TopicPartition]): Unit = {
        println("onPartitionsRevoked")
        val iter = collection.iterator()
        while (iter.hasNext) {
          println(iter.next())
        }
      }

      override def onPartitionsAssigned(collection: util.Collection[TopicPartition]): Unit = {
        println("onPartitionsAssigned")
        val iter = collection.iterator()
        while (iter.hasNext) {
          println(iter.next())
        }
      }
    })

    while (true) {
      val records = consumer.poll(1)
      if (!records.isEmpty) {
        println(s"-----------${records.count()}-----------")
        val iter = records.iterator()
        while (iter.hasNext) {
          val record = iter.next()
          val key = record.key()
          val value = record.value()
          val offset = record.offset()
          val partition = record.partition()
          println(s"$key\t$value\t$offset\t$partition")
        }
      }
    }

  }

}
