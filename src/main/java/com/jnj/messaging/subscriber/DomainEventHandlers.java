package com.jnj.messaging.subscriber;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.jnj.messaging.common.Message;

import static java.util.stream.Collectors.toSet;

public class DomainEventHandlers {
  private List<DomainEventHandler> handlers;

  public DomainEventHandlers(List<DomainEventHandler> handlers) {
    this.handlers = handlers;
  }

  public Set<String> getAggregateTypesAndEvents() {
    return handlers.stream().map(DomainEventHandler::getAggregateType).collect(toSet());
  }

  public List<DomainEventHandler> getHandlers() {
    return handlers;
  }

  public Optional<DomainEventHandler> findTargetMethod(Message message) {
    return handlers.stream().filter(h -> h.handles(message)).findFirst();
  }
}
