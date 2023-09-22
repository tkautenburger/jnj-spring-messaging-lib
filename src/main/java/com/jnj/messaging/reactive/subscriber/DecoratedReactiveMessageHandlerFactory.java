package com.jnj.messaging.reactive.subscriber;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.reactivestreams.Publisher;

import com.jnj.messaging.common.SubscriberIdAndMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DecoratedReactiveMessageHandlerFactory {

  private List<ReactiveMessageHandlerDecorator> decorators;

  public DecoratedReactiveMessageHandlerFactory(List<ReactiveMessageHandlerDecorator> decorators) {
    this.decorators = decorators;
    decorators.sort(Comparator.comparingInt(ReactiveMessageHandlerDecorator::getOrder));
    log.info("Found the following ReactiveMessageHandlerDecorators {}", decorators);
  }

  public Function<SubscriberIdAndMessage, Publisher<?>> decorate(ReactiveMessageHandler reactiveMessageHandler) {
    return subscriberIdAndMessage ->
            new ReactiveMessageHandlerDecoratorChain(decorators, reactiveMessageHandler).next(subscriberIdAndMessage);
  }
}
