package com.jnj.messaging.subscriber;

import java.util.Set;

public interface MessageConsumer {
  MessageSubscription subscribe(String subscriberId, Set<String> channels, MessageHandler handler);
  String getId();
  void close();
}
