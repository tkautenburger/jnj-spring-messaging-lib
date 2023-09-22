package com.jnj.messaging.publisher;

import java.util.Collections;
import java.util.Map;

import com.jnj.messaging.common.DomainEvent;
import com.jnj.messaging.util.EventMessageUtil;

public class DomainEventPublisherImpl implements DomainEventPublisher {
  private MessageProducer messageProducer;

  public DomainEventPublisherImpl(MessageProducer messageProducer) {
    this.messageProducer = messageProducer;
  }

  @Override
  public void publish(String aggregateType, Object aggregateId, DomainEvent domainEvent) {
    publish(aggregateType, aggregateId, Collections.emptyMap(), domainEvent);
  }

  @Override
  public void publish(String aggregateType, Object aggregateId,
      Map<String, String> headers, DomainEvent event) {
      messageProducer.send(aggregateType,
          EventMessageUtil.generateDomainEventMessage(aggregateType, aggregateId, headers, event,
              event.getClass().getName()));

  }
}
