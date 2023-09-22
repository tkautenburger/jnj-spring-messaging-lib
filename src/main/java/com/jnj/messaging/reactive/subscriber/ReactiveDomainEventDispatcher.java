package com.jnj.messaging.reactive.subscriber;

import java.util.Optional;

import org.reactivestreams.Publisher;

import com.jnj.messaging.common.DomainEvent;
import com.jnj.messaging.common.EventMessageHeaders;
import com.jnj.messaging.common.Message;
import com.jnj.messaging.subscriber.DomainEventEnvelopeImpl;
import com.jnj.messaging.util.JsonMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Slf4j
public class ReactiveDomainEventDispatcher {

  private final String eventDispatcherId;
  private ReactiveDomainEventHandlers domainEventHandlers;
  private ReactiveMessageConsumer messageConsumer;

  public ReactiveDomainEventDispatcher(String eventDispatcherId,
                                       ReactiveDomainEventHandlers domainEventHandlers,
                                       ReactiveMessageConsumer messageConsumer) {

    this.eventDispatcherId = eventDispatcherId;
    this.domainEventHandlers = domainEventHandlers;
    this.messageConsumer = messageConsumer;
  }

  public void initialize() {
    log.info("Initializing reactive domain event dispatcher");
    messageConsumer.subscribe(eventDispatcherId, domainEventHandlers.getAggregateTypesAndEvents(), this::messageHandler);
    log.info("Initialized reactive domain event dispatcher");
  }

  public Publisher<?> messageHandler(Message message) {
    String aggregateType = message.getRequiredHeader(EventMessageHeaders.AGGREGATE_TYPE);

    Optional<ReactiveDomainEventHandler> handler = domainEventHandlers.findTargetMethod(message);

    if (!handler.isPresent()) {
      return Mono.empty();
    }

    DomainEvent param = JsonMapper.fromJson(message.getPayload(), handler.get().getEventClass());

    return handler.get().invoke(new DomainEventEnvelopeImpl<>(message,
            aggregateType,
            message.getRequiredHeader(EventMessageHeaders.AGGREGATE_ID),
            message.getRequiredHeader(Message.ID),
            param));
  }

}
