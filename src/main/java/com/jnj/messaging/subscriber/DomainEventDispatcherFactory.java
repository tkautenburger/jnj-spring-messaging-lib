package com.jnj.messaging.subscriber;

public class DomainEventDispatcherFactory {
  protected MessageConsumer messageConsumer;

  public DomainEventDispatcherFactory(MessageConsumer messageConsumer) {
    this.messageConsumer = messageConsumer;
  }

  public DomainEventDispatcher make(String eventDispatcherId, DomainEventHandlers domainEventHandlers) {
    DomainEventDispatcher domainEventDispatcher = new DomainEventDispatcher(eventDispatcherId, domainEventHandlers,
        messageConsumer);
    domainEventDispatcher.initialize();
    return domainEventDispatcher;
  }
}
