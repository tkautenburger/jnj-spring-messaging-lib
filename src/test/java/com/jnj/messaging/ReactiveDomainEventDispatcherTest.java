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
import com.jnj.messaging.publisher.HttpDateHeaderFormatUtil;
import com.jnj.messaging.reactive.subscriber.ReactiveDomainEventDispatcher;
import com.jnj.messaging.reactive.subscriber.ReactiveDomainEventHandlers;
import com.jnj.messaging.reactive.subscriber.ReactiveDomainEventHandlersBuilder;
import com.jnj.messaging.reactive.subscriber.ReactiveMessageConsumer;
import com.jnj.messaging.subscriber.DomainEventEnvelope;
import com.jnj.messaging.util.EventMessageUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class ReactiveDomainEventDispatcherTest {
  private String eventDispatcherId;
  private String aggregateType = "AggregateType";

  private String messageId = "message-" + UUID.randomUUID().toString();

  class MyTarget {

    public BlockingQueue<DomainEventEnvelope<?>> queue = new LinkedBlockingDeque<>();

    public ReactiveDomainEventHandlers domainEventHandlers() {
      return ReactiveDomainEventHandlersBuilder
          .forAggregateType(aggregateType)
          .onEvent(CreatedEvent.class, this::handleCreatedEvent)
          .onEvent(UpdatedEvent.class, this::handleUpdatedEvent)
          .build();
    }

    public Mono<Void> handleCreatedEvent(DomainEventEnvelope<CreatedEvent> envelope) {
      log.info("in create event handler to handle event: {}", envelope.getEvent());
      queue.add(envelope);
      return Mono.empty();
    }

    public Mono<Void> handleUpdatedEvent(DomainEventEnvelope<UpdatedEvent> envelope) {
      log.info("in update event handler to handle event: {}", envelope.getEvent());
      queue.add(envelope);
      return Mono.empty();
    }
  }

  @Data
  static class CreatedEvent implements DomainEvent {
    private UUID id;
    private String value = "hello world";
  }

  @Data
  static class UpdatedEvent implements DomainEvent {

    private UUID id;
    private String value = "hello universe";
  }

  @Test
  public void shouldDispatchMessage() throws JsonMappingException, JsonProcessingException, InterruptedException {
    MyTarget target = new MyTarget();

    ReactiveMessageConsumer messageConsumer = mock(ReactiveMessageConsumer.class);

    ReactiveDomainEventDispatcher dispatcher = new ReactiveDomainEventDispatcher(eventDispatcherId,
        target.domainEventHandlers(),
        messageConsumer);

    dispatcher.initialize();

    UUID aggregateId1 = UUID.randomUUID();
    CreatedEvent event1 = new CreatedEvent();
    event1.setId(aggregateId1);

    Message msg1 = EventMessageUtil.generateDomainEventMessage(aggregateType,
        aggregateId1,
        Collections.singletonMap(Message.ID, messageId),
        event1, event1.getClass().getName());

        // optionally add additional headers to the message
        msg1.setHeader("sentAt", HttpDateHeaderFormatUtil.nowAsHttpDateString());

    UUID aggregateId2 = UUID.randomUUID();
    UpdatedEvent event2 = new UpdatedEvent();
    event2.setId(aggregateId2);

    Message msg2 = EventMessageUtil.generateDomainEventMessage(aggregateType,
        aggregateId2,
        Collections.singletonMap(Message.ID, messageId),
        event2, event2.getClass().getName());

        // optionally add additional headers to the message
        msg2.setHeader("sentAt", HttpDateHeaderFormatUtil.nowAsHttpDateString());

    // for testing we call this manually, in production method is called by the
    // MessageConsumer subscription
    dispatcher.messageHandler(msg1);
    dispatcher.messageHandler(msg2);

    DomainEventEnvelope<?> dee = target.queue.take();

    assertNotNull(dee);

    log.info("Envelope: {}", dee);

    assertEquals(aggregateId1.toString(), dee.getAggregateId());
    assertEquals(aggregateType, dee.getAggregateType());
    assertEquals(messageId, dee.getEventId());

    dee = target.queue.take();

    assertNotNull(dee);

    log.info("Envelope: {}", dee);

    assertEquals(aggregateId2.toString(), dee.getAggregateId());
    assertEquals(aggregateType, dee.getAggregateType());
    assertEquals(messageId, dee.getEventId());
  }
}
