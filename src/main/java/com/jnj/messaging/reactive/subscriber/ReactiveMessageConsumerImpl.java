package com.jnj.messaging.reactive.subscriber;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.reactivestreams.Publisher;

import com.jnj.messaging.common.ChannelMapping;
import com.jnj.messaging.common.SubscriberIdAndMessage;
import com.jnj.messaging.subscriber.MessageSubscription;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ReactiveMessageConsumerImpl implements ReactiveMessageConsumer {
  private ChannelMapping channelMapping;
  private ReactiveMessageConsumerImplementation target;
  private DecoratedReactiveMessageHandlerFactory decoratedMessageHandlerFactory;

  public ReactiveMessageConsumerImpl(ChannelMapping channelMapping,
                                     ReactiveMessageConsumerImplementation target,
                                     DecoratedReactiveMessageHandlerFactory decoratedMessageHandlerFactory) {
    this.channelMapping = channelMapping;
    this.target = target;
    this.decoratedMessageHandlerFactory = decoratedMessageHandlerFactory;
  }

  @Override
  public MessageSubscription subscribe(String subscriberId, Set<String> channels, ReactiveMessageHandler handler) {
    log.info("Subscribing (reactive): subscriberId = {}, channels = {}", subscriberId, channels);

    Function<SubscriberIdAndMessage, Publisher<?>> decoratedHandler =
            decoratedMessageHandlerFactory.decorate(handler);

    MessageSubscription messageSubscription =
            target.subscribe(
                    subscriberId,
                    channels.stream().map(channelMapping::transform).collect(Collectors.toSet()),
                    message -> decoratedHandler
                            .apply(new SubscriberIdAndMessage(subscriberId, message)));

    log.info("Subscribed (reactive): subscriberId = {}, channels = {}", subscriberId, channels);

    return messageSubscription;
  }

  @Override
  public String getId() {
    return target.getId();
  }

  @Override
  public void close() {
    log.info("Closing reactive consumer");

    target.close();

    log.info("Closed reactive consumer");
  }

}