package com.jnj.messaging.reactive.subscriber;

import java.util.Set;

import com.jnj.messaging.subscriber.MessageSubscription;

public interface ReactiveMessageConsumer {
  MessageSubscription subscribe(String subscriberId, Set<String> channels, ReactiveMessageHandler handler);
  String getId();
  void close();  
}
