package com.oxywire.oxytowns.command.argument;

import com.oxywire.oxytowns.config.messaging.Message;

public final class MessageArgumentParseException extends RuntimeException {

    private final Message message;

    public MessageArgumentParseException(final Message message) {
        this.message = message;
    }

    public Message getConfiguredMessage() {
        return this.message;
    }
}
