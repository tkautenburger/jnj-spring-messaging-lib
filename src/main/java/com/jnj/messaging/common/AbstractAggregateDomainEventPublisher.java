package com.jnj.messaging.common;

import java.util.function.Function;

import com.jnj.messaging.publisher.DomainEventPublisher;

// this is currently not used 

public abstract class AbstractAggregateDomainEventPublisher<A, E extends DomainEvent> {
  private Function<A, Object> idSupplier;
  private DomainEventPublisher eventPublisher;
  private Class<A> aggregateType;

  protected AbstractAggregateDomainEventPublisher(DomainEventPublisher eventPublisher,
                                                  Class<A> aggregateType,
                                                  Function<A, Object> idSupplier) {
    this.eventPublisher = eventPublisher;
    this.aggregateType = aggregateType;
    this.idSupplier = idSupplier;
  }

  public Class<A> getAggregateType() {
    return aggregateType;
  }

  public void publish(A aggregate, E event) {
    eventPublisher.publish(aggregateType, idSupplier.apply(aggregate), (DomainEvent) event);
  }}
