package com.jnj.messaging.reactive.subscriber;

import java.util.Set;

import com.jnj.messaging.subscriber.MessageSubscription;

public interface ReactiveMessageConsumerImplementation {
  MessageSubscription subscribe(String subscriberId, Set<String> channels, ReactiveMessageHandler handler);
  String getId();
  void close();  
}
