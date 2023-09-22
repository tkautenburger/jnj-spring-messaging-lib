package com.jnj.messaging.subscriber;

import java.util.Optional;

import com.jnj.messaging.common.EventMessageHeaders;
import com.jnj.messaging.common.Message;
import com.jnj.messaging.util.JsonMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DomainEventDispatcher {

  private final String eventDispatcherId;
  private DomainEventHandlers domainEventHandlers;
  private MessageConsumer messageConsumer;

  public DomainEventDispatcher(String eventDispatcherId, DomainEventHandlers domainEventHandlers,
      MessageConsumer messageConsumer) {
    this.eventDispatcherId = eventDispatcherId;
    this.domainEventHandlers = domainEventHandlers;
    this.messageConsumer = messageConsumer;
  }

  public void initialize() {
    log.info("Initializing domain event dispatcher");
    messageConsumer.subscribe(eventDispatcherId, domainEventHandlers.getAggregateTypesAndEvents(),
        handler -> messageHandler(handler));
        log.info("Initialized domain event dispatcher");
  }

  public void messageHandler(Message message) {
    String aggregateType = message.getRequiredHeader(EventMessageHeaders.AGGREGATE_TYPE);

    Optional<DomainEventHandler> handler = domainEventHandlers.findTargetMethod(message);

    if (!handler.isPresent()) {
      return;
    }
    handler.get().invoke(new DomainEventEnvelopeImpl<>(message,
        aggregateType,
        message.getRequiredHeader(EventMessageHeaders.AGGREGATE_ID),
        message.getRequiredHeader(Message.ID),
        JsonMapper.fromJson(message.getPayload(), handler.get().getEventClass())));
  }
}
