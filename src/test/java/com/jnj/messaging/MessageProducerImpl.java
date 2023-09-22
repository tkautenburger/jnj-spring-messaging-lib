package com.jnj.messaging;

import com.jnj.messaging.common.Message;
import com.jnj.messaging.publisher.MessageProducer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageProducerImpl implements MessageProducer {

  @Override
  public void send(String destination, Message message) {
    log.info("Sending message {} to destination {}", message, destination);
  }
  
}
