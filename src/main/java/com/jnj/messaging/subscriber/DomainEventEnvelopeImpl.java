package com.jnj.messaging.subscriber;

import com.jnj.messaging.common.DomainEvent;
import com.jnj.messaging.common.Message;

public class DomainEventEnvelopeImpl<T extends DomainEvent> implements DomainEventEnvelope<T> {

  private Message message;
  private final String aggregateType;
  private String aggregateId;
  private final String eventId;
  private T event;

  public DomainEventEnvelopeImpl(Message message, String aggregateType, String aggregateId, String eventId, T event) {
    this.message = message;
    this.aggregateType = aggregateType;
    this.aggregateId = aggregateId;
    this.eventId = eventId;
    this.event = event;
  }

  @Override
  public String getAggregateId() {
    return aggregateId;
  }

  @Override
  public Message getMessage() {
    return message;
  }

  public T getEvent() {
    return event;
  }

  @Override
  public String getAggregateType() {
    return aggregateType;
  }

  @Override
  public String getEventId() {
    return eventId;
  }

  @Override
  public String toString() {
    return "DomainEventEnvelopeImpl [message=" + message + ", aggregateType=" + aggregateType + ", aggregateId="
        + aggregateId + ", eventId=" + eventId + ", event=" + event + "]";
  }

}
