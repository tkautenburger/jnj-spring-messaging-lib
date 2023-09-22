package com.jnj.messaging;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.jnj.messaging.common.DefaultChannelMapping;
import com.jnj.messaging.common.DomainEvent;
import com.jnj.messaging.reactive.publisher.ReactiveDomainEventPublisher;
import com.jnj.messaging.reactive.publisher.ReactiveMessageProducer;

import lombok.Data;

public class ReactiveDomainEventPublisherTest {

  private String aggregateType ="Aggregate";
  private String aggregateId = UUID.randomUUID().toString();

  @Data
  private class MyDomainEvent implements DomainEvent {
    private String id;
    private String name = "hello world";
  }

  @Test
  public void shouldPublishMessage() {
    Map<String, String> channelmap = new HashMap<>();
    channelmap.put("from", "to");

    ReactiveMessageProducer messageProducer = new ReactiveMessageProducer(null, new DefaultChannelMapping(channelmap), new ReactiveMessageProducerImpl());

    ReactiveDomainEventPublisher publisher = new ReactiveDomainEventPublisher(messageProducer);
    
    MyDomainEvent event = new MyDomainEvent();
    event.setId(aggregateId);

    publisher.publish(aggregateType, aggregateId, Collections.emptyMap(), event);

  }
  
}
