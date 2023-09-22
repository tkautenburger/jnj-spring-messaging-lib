package com.jnj.messaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jnj.messaging.common.DomainEvent;
import com.jnj.messaging.common.Message;
import com.jnj.messaging.subscriber.DomainEventDispatcher;
import com.jnj.messaging.subscriber.DomainEventEnvelope;
import com.jnj.messaging.subscriber.DomainEventHandlers;
import com.jnj.messaging.subscriber.DomainEventHandlersBuilder;
import com.jnj.messaging.subscriber.MessageConsumer;
import com.jnj.messaging.util.EventMessageUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DomainEventDispatcherTest {
  private String eventDispatcherId;
  private String aggregateType = "AggregateType";

  private String aggregateId = UUID.randomUUID().toString();
  private String messageId = "message-" +  UUID.randomUUID().toString();

  class MyTarget {

    public BlockingQueue<DomainEventEnvelope<?>> queue = new LinkedBlockingDeque<>();

    public DomainEventHandlers domainEventHandlers() {
      return DomainEventHandlersBuilder
          .forAggregateType(aggregateType)
          .onEvent(MyDomainEvent.class, this::handleTestEvent)
          .build();
    }

    public void handleTestEvent(DomainEventEnvelope<MyDomainEvent> envelope) {
      log.info("in event handler to handle event: {}", envelope.getEvent());
      queue.add(envelope);
    }

  }

  @Data
  static class MyDomainEvent implements DomainEvent {

      String id = "4711";
      String value = "hello world";
  }

  @Test
  public void shouldDispatchMessage() throws JsonMappingException, JsonProcessingException {
    MyTarget target = new MyTarget();

    MessageConsumer messageConsumer = mock(MessageConsumer.class);

    DomainEventDispatcher dispatcher = new DomainEventDispatcher(eventDispatcherId, target.domainEventHandlers(),
        messageConsumer);

    dispatcher.initialize();

    dispatcher.messageHandler(EventMessageUtil.generateDomainEventMessage(aggregateType,
        aggregateId,
        Collections.singletonMap(Message.ID, messageId),
        new MyDomainEvent(), MyDomainEvent.class.getName()));

    DomainEventEnvelope<?> dee = target.queue.peek();

    assertNotNull(dee);

    log.info("Envelope: {}", dee);

    assertEquals(aggregateId, dee.getAggregateId());
    assertEquals(aggregateType, dee.getAggregateType());
    assertEquals(messageId, dee.getEventId());

  }
}
