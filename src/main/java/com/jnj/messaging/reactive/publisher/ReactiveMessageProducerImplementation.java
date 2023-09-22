package com.jnj.messaging.reactive.publisher;

import com.jnj.messaging.common.Message;

import reactor.core.publisher.Mono;

public interface ReactiveMessageProducerImplementation {

    Mono<Message> send(Message message);
}
