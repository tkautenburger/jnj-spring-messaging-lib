package com.jnj.messaging.subscriber;

import com.jnj.messaging.common.DomainEvent;
import com.jnj.messaging.common.Message;

public interface DomainEventEnvelope<T extends DomainEvent> {
  String getAggregateId();

  Message getMessage();

  String getAggregateType();

  String getEventId();

  T getEvent();
}
