package com.jnj.messaging.common;

// this is currently not used hence the DomainEventPublisheron the producer side
// and DomainEventDispatcher on the consumer site map event types based on their
// class names

public interface DomainEventNameMapping {
  String eventToExternalEventType(String aggregateType, DomainEvent event);
  String externalEventTypeToEventClassName(String aggregateType, String eventTypeHeader);
}
