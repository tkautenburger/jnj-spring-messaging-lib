package com.jnj.messaging.reactive.publisher;

import static java.util.function.Function.identity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.jnj.messaging.common.DomainEvent;
import com.jnj.messaging.common.Message;
import com.jnj.messaging.util.EventMessageUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactiveDomainEventPublisher {

  private ReactiveMessageProducer reactiveMessageProducer;

  public ReactiveDomainEventPublisher(ReactiveMessageProducer reactiveMessageProducer) {
    this.reactiveMessageProducer = reactiveMessageProducer;
  }

  public DomainEventPublishingBuilder.AggregateTypeStep aggregateType(String value) {
    return new DomainEventPublishingBuilder(this).aggregateType(value);
  }

  public DomainEventPublishingBuilder.AggregateTypeStep aggregateType(Class<?> value) {
    return new DomainEventPublishingBuilder(this).aggregateType(value);
  }

  Mono<List<Message>> publish(List<DomainEventPublishingBuilder.EventContainer> events) {
    List<Mono<List<Message>>> result = new ArrayList<>();

    for (DomainEventPublishingBuilder.EventContainer container : events) {
      Mono<List<Message>> iteration = publish(container.getAggregateType(),
              container.getAggregateId(), container.getHeaders(), container.getDomainEvents());

        result.add(iteration);
    }

    return Mono.zip(result, objects -> {
      ArrayList<Message> messages = new ArrayList<>();

      for (Object o : objects) {
        messages.addAll((List<Message>)o);
      }

      return messages;
    });
  }

  public Mono<Message> publish(String aggregateType, Object aggregateId, DomainEvent domainEvent) {
    return publish(aggregateType, aggregateId, Collections.emptyMap(), domainEvent);
  }

  public Mono<List<Message>> publish(String aggregateType, Object aggregateId, List<DomainEvent> domainEvents) {
    return publish(aggregateType, aggregateId, Collections.emptyMap(), domainEvents);
  }

  public Mono<Message> publish(String aggregateType, Object aggregateId, Map<String, String> headers, DomainEvent domainEvent) {
    return reactiveMessageProducer.send(aggregateType, EventMessageUtil.generateDomainEventMessage(aggregateType, aggregateId, headers, domainEvent,
            domainEvent.getClass().getName()));
  }

  public Mono<List<Message>> publish(String aggregateType, Object aggregateId, Map<String, String> headers, List<DomainEvent> domainEvents) {
    Stream<Mono<Message>> messages = domainEvents
            .stream()
            .map(event -> reactiveMessageProducer.send(aggregateType,
                    EventMessageUtil.generateDomainEventMessage(aggregateType, aggregateId, headers, event,
                            event.getClass().getName())));

    return Flux.fromStream(messages).flatMap(identity()).collectList();
  }

  public Mono<Message> publish(Class<?> aggregateType, Object aggregateId, DomainEvent domainEvent) {
    return publish(aggregateType.getName(), aggregateId, domainEvent);
  }

  public Mono<List<Message>> publish(Class<?> aggregateType, Object aggregateId, List<DomainEvent> domainEvents) {
    return publish(aggregateType.getName(), aggregateId, domainEvents);
  }
}