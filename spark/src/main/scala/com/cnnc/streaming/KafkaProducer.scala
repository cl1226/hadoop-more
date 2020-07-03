package com.cnnc.streaming

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object KafkaProducer {

  def main(args: Array[String]): Unit = {

    val prop = new Properties()
    prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.41.193:9092")
    prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])

    val producer = new KafkaProducer[String, String](prop)

    while (true) {
      for (i <- 1 to 3; j <- 1 to 3) {
        val record = new ProducerRecord[String, String]("topic02", s"key$i", s"value$j")
        val records = producer.send(record)
        val metadata = records.get()
        val partition = metadata.partition()
        val offset = metadata.offset()
        println(s"key$i\tvalue$j\t$partition\t$offset")
      }

      Thread.sleep(5000)
    }

    producer.close()

  }

}
