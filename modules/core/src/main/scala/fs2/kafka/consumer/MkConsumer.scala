/*
 * Copyright 2018-2025 OVO Energy Limited
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package fs2.kafka.consumer

import fs2.kafka.{ConsumerSettings, KafkaByteConsumer}
import fs2.kafka.internal.converters.collection.*

import org.apache.kafka.common.serialization.ByteArrayDeserializer

/**
  * A capability trait representing the ability to instantiate the Java `Consumer` that underlies
  * the fs2-kafka `KafkaConsumer`. This is needed in order to instantiate
  * [[fs2.kafka.KafkaConsumer]].<br><br>
  *
  * By default, the instance provided by [[MkConsumer.mkConsumerForSync]] will be used. However this
  * behaviour can be overridden, e.g. for testing purposes, by placing an alternative implicit
  * instance in lexical scope.
  */
trait MkConsumer {
  def apply[G[_]](settings: ConsumerSettings[G, ?, ?]): KafkaByteConsumer
}

object MkConsumer {

  implicit def mkConsumer: MkConsumer =
    new MkConsumer {

      def apply[G[_]](settings: ConsumerSettings[G, ?, ?]): KafkaByteConsumer = {
        val byteArrayDeserializer = new ByteArrayDeserializer
        new org.apache.kafka.clients.consumer.KafkaConsumer(
          (settings.properties: Map[String, AnyRef]).asJava,
          byteArrayDeserializer,
          byteArrayDeserializer
        )
      }

    }

}
