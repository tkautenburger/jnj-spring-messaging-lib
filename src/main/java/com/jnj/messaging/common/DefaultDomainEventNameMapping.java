package com.jnj.messaging.common;

// this is currently not used hence the DomainEventPublisheron the producer side
// and DomainEventDispatcher on the consumer site map event types based on their
// class names

public class DefaultDomainEventNameMapping implements DomainEventNameMapping {

  @Override
  public String eventToExternalEventType(String aggregateType, DomainEvent event) {
    return event.getClass().getName();
  }

  @Override
  public String externalEventTypeToEventClassName(String aggregateType, String eventTypeHeader) {
    return eventTypeHeader;
  }
}
