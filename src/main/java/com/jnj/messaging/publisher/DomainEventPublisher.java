package com.jnj.messaging.publisher;

import java.util.Map;

import com.jnj.messaging.common.DomainEvent;

public interface DomainEventPublisher {

  void publish(String aggregateType, Object aggregateId, DomainEvent event);

  void publish(String aggregateType, Object aggregateId, Map<String, String> headers, DomainEvent event);

  default void publish(Class<?> aggregateType, Object aggregateId, DomainEvent event) {
    publish(aggregateType.getName(), aggregateId, event);
  }
}
