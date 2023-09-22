package com.jnj.messaging.reactive.subscriber;


public class ReactiveDomainEventDispatcherFactory {
  protected ReactiveMessageConsumer messageConsumer;

  public ReactiveDomainEventDispatcherFactory(ReactiveMessageConsumer messageConsumer) {
    this.messageConsumer = messageConsumer;
  }

  public ReactiveDomainEventDispatcher make(String eventDispatcherId, ReactiveDomainEventHandlers domainEventHandlers) {
    ReactiveDomainEventDispatcher reactiveDomainEventDispatcher = new ReactiveDomainEventDispatcher(eventDispatcherId, domainEventHandlers, messageConsumer);
    reactiveDomainEventDispatcher.initialize();
    return reactiveDomainEventDispatcher;
  }
}
