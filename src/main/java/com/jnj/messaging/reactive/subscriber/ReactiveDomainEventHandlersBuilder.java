package com.jnj.messaging.reactive.subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.reactivestreams.Publisher;

import com.jnj.messaging.common.DomainEvent;
import com.jnj.messaging.subscriber.DomainEventEnvelope;

public class ReactiveDomainEventHandlersBuilder {
  private String aggregateType;
  private List<ReactiveDomainEventHandler> handlers = new ArrayList<>();

  public ReactiveDomainEventHandlersBuilder(String aggregateType) {
    this.aggregateType = aggregateType;
  }

  public static ReactiveDomainEventHandlersBuilder forAggregateType(String aggregateType) {
    return new ReactiveDomainEventHandlersBuilder(aggregateType);
  }

  public <E extends DomainEvent> ReactiveDomainEventHandlersBuilder onEvent(Class<E> eventClass,
      Function<DomainEventEnvelope<E>, Publisher<?>> handler) {

    handlers.add(new ReactiveDomainEventHandler(aggregateType,
        ((Class<DomainEvent>) eventClass),
        (e) -> handler.apply((DomainEventEnvelope<E>) e)));

    return this;
  }

  public ReactiveDomainEventHandlersBuilder andForAggregateType(String aggregateType) {
    this.aggregateType = aggregateType;
    return this;
  }

  public ReactiveDomainEventHandlers build() {
    return new ReactiveDomainEventHandlers(handlers);
  }
}
