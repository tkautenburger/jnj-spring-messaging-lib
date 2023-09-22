package com.jnj.messaging.subscriber;

import java.util.function.Consumer;

import com.jnj.messaging.common.Message;

public interface MessageHandler extends Consumer<Message> {
}
