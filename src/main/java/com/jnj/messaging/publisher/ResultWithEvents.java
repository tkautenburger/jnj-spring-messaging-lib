package com.jnj.messaging.publisher;

import java.util.Arrays;
import java.util.List;

import com.jnj.messaging.common.DomainEvent;

public class ResultWithEvents<T> {
  public final T result;
  public final List<DomainEvent> events;

  public ResultWithEvents(T result, List<DomainEvent> events) {
    this.result = result;
    this.events = events;
  }

  public ResultWithEvents(T result, DomainEvent... events) {
    this.result = result;
    this.events = Arrays.asList(events);
  }
}
