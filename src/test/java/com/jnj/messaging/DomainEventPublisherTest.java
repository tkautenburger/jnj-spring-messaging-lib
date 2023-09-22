package com.jnj.messaging;

import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.jnj.messaging.common.DomainEvent;
import com.jnj.messaging.publisher.DomainEventPublisherImpl;

import lombok.Data;

public class DomainEventPublisherTest {

  private String aggregateType ="Aggregate";
  private String aggregateId = UUID.randomUUID().toString();

  @Data
  private class MyDomainEvent implements DomainEvent {
    private String id;
    private String name = "hello world";
  }

  @Test
  public void shouldPublishMessage() {
    MessageProducerImpl messageProducer = new MessageProducerImpl();

    DomainEventPublisherImpl publisher = new DomainEventPublisherImpl(messageProducer);
    
    MyDomainEvent event = new MyDomainEvent();
    event.setId(aggregateId);

    publisher.publish(aggregateType, aggregateId, Collections.emptyMap(), event);

  }
  
}
