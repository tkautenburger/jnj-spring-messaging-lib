package com.jnj.messaging.publisher;

import com.jnj.messaging.common.Message;

public interface MessageProducer {
  /**
   * Send a message
   * @param destination the destination channel
   * @param message the message to doSend
   * @see Message
   */
  void send(String destination, Message message);

}
