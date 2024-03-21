package com.oxywire.oxytowns.config.internal.serializer;

import com.oxywire.oxytowns.config.messaging.Message;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class MessageSerializer implements TypeSerializer<Message> {

    public static final MessageSerializer INSTANCE = new MessageSerializer();

    @Override
    public Message deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        if (node.getString() == null) {
            return (Message) ObjectMapper.factory().asTypeSerializer().deserialize(type, node);
        }

        return new Message().setMessage(node.getString());
    }

    @Override
    public void serialize(final Type type, @Nullable Message obj, final ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            throw new SerializationException("Message must not be null.");
        }

        ObjectMapper.factory().asTypeSerializer().serialize(type, obj, node);
    }
}
