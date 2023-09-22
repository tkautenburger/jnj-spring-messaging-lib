package com.jnj.messaging.reactive.subscriber;

import java.util.List;

import org.reactivestreams.Publisher;

import com.jnj.messaging.common.SubscriberIdAndMessage;

import reactor.core.publisher.Mono;

public class ReactiveMessageHandlerDecoratorChain {
  private List<ReactiveMessageHandlerDecorator> decorators;
  private ReactiveMessageHandler reactiveMessageHandler;

  public ReactiveMessageHandlerDecoratorChain(List<ReactiveMessageHandlerDecorator> decorators,
                                              ReactiveMessageHandler reactiveMessageHandler) {
    this.decorators = decorators;
    this.reactiveMessageHandler = reactiveMessageHandler;
  }

  public Publisher<?> next(SubscriberIdAndMessage subscriberIdAndMessage) {

    if (decorators.isEmpty())
      return Mono.defer(() -> Mono.from(reactiveMessageHandler.apply(subscriberIdAndMessage.getMessage())));
    else {
      return decorators.get(0).accept(subscriberIdAndMessage,
            new ReactiveMessageHandlerDecoratorChain(decorators.subList(1, decorators.size()), reactiveMessageHandler));
    }
  }  
}
