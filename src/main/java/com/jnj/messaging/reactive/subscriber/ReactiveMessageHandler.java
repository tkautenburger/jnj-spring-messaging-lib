package com.jnj.messaging.reactive.subscriber;

import java.util.function.Function;

import org.reactivestreams.Publisher;

import com.jnj.messaging.common.Message;

public interface ReactiveMessageHandler extends Function<Message, Publisher<?>> {
}  

