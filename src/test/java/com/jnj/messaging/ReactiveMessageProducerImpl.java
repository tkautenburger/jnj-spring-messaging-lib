package com.jnj.messaging;

import com.jnj.messaging.common.Message;
import com.jnj.messaging.reactive.publisher.ReactiveMessageProducerImplementation;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class ReactiveMessageProducerImpl implements ReactiveMessageProducerImplementation {

  @Override
  public Mono<Message> send(Message message) {
    log.info("Sending message {} to destination {}", message, message.getHeader(Message.DESTINATION).get());
    return Mono.just(message);
  }
  
}
