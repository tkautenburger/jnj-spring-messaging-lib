package com.jnj.messaging.util;

import java.util.Map;

import com.jnj.messaging.common.DomainEvent;
import com.jnj.messaging.common.EventMessageHeaders;
import com.jnj.messaging.common.Message;
import com.jnj.messaging.publisher.MessageBuilder;


public class EventMessageUtil {
  public static Message generateDomainEventMessage(String aggregateType,
      Object aggregateId,
      Map<String, String> headers,
      DomainEvent event,
      String eventType) {

    String aggregateIdAsString = aggregateId.toString();
    return MessageBuilder
        .withPayload(JsonMapper.toJson(event))
        .withExtraHeaders("", headers)
        .withHeader(Message.PARTITION_ID, aggregateIdAsString)
        .withHeader(EventMessageHeaders.AGGREGATE_ID, aggregateIdAsString)
        .withHeader(EventMessageHeaders.AGGREGATE_TYPE, aggregateType)
        .withHeader(EventMessageHeaders.EVENT_TYPE, eventType)
        .build();
  }
}
