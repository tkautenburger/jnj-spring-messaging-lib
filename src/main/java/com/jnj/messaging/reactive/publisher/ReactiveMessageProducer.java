package com.jnj.messaging.reactive.publisher;

import java.util.Arrays;

import com.jnj.messaging.common.ChannelMapping;
import com.jnj.messaging.common.Message;
import com.jnj.messaging.common.MessageInterceptor;
import com.jnj.messaging.publisher.HttpDateHeaderFormatUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class ReactiveMessageProducer {

  private final MessageInterceptor[] messageInterceptors;
  private final ChannelMapping channelMapping;
  private final ReactiveMessageProducerImplementation implementation;

  public ReactiveMessageProducer(MessageInterceptor[] messageInterceptors,
      ChannelMapping channelMapping,
      ReactiveMessageProducerImplementation implementation) {
    this.messageInterceptors = messageInterceptors;
    this.channelMapping = channelMapping;
    this.implementation = implementation;

  }

  //private void preSend(Message message) {
  //  Arrays.stream(messageInterceptors).forEach(mi -> mi.preSend(message));
  //}

  private void postSend(Message message, RuntimeException e) {
    Arrays.stream(messageInterceptors).forEach(mi -> mi.postSend(message, e));
  }

  public Mono<Message> send(String destination, Message message) {
    prepareMessageHeaders(destination, message);
    return send(message);
  }

  protected void prepareMessageHeaders(String destination, Message message) {
    message.getHeaders().put(Message.DESTINATION, channelMapping.transform(destination));
    message.getHeaders().put(Message.DATE, HttpDateHeaderFormatUtil.nowAsHttpDateString());
  }

  protected Mono<Message> send(Message message) {
    // preSend(message);

    return implementation
        .send(message)
        .doOnError(throwable -> {
          log.error("Sending failed", throwable);
          if (throwable instanceof RuntimeException) {
            postSend(message, (RuntimeException) throwable);
          }
          throw new RuntimeException(throwable);
        });
        //.doOnSuccess(msg -> postSend(message, null));
  }
}
