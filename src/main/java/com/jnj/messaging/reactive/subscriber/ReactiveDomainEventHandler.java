package com.jnj.messaging.reactive.subscriber;

import java.util.function.Function;

import org.reactivestreams.Publisher;

import com.jnj.messaging.common.DomainEvent;
import com.jnj.messaging.common.EventMessageHeaders;
import com.jnj.messaging.common.Message;
import com.jnj.messaging.subscriber.DomainEventEnvelope;

public class ReactiveDomainEventHandler {
  private String aggregateType;
  private final Class<DomainEvent> eventClass;
  private final Function<DomainEventEnvelope<DomainEvent>, Publisher<?>> handler;

  public ReactiveDomainEventHandler(String aggregateType, Class<DomainEvent> eventClass,
      Function<DomainEventEnvelope<DomainEvent>, Publisher<?>> handler) {
    this.aggregateType = aggregateType;
    this.eventClass = eventClass;
    this.handler = handler;
  }

  public boolean handles(Message message) {
    return aggregateType.equals(message.getRequiredHeader(EventMessageHeaders.AGGREGATE_TYPE))
        && eventClass.getName().equals(message.getRequiredHeader(EventMessageHeaders.EVENT_TYPE));
  }

  public Publisher<?> invoke(DomainEventEnvelope<DomainEvent> dee) {
    return handler.apply(dee);
  }

  public Class<DomainEvent> getEventClass() {
    return eventClass;
  }

  public String getAggregateType() {
    return aggregateType;
  }
}
