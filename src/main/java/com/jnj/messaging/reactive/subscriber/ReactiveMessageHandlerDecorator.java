package com.jnj.messaging.reactive.subscriber;

import org.reactivestreams.Publisher;

import com.jnj.messaging.common.SubscriberIdAndMessage;

public interface ReactiveMessageHandlerDecorator {
  Publisher<?> accept(SubscriberIdAndMessage subscriberIdAndMessage,
      ReactiveMessageHandlerDecoratorChain decoratorChain);

  int getOrder();
}
