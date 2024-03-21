package com.oxywire.oxytowns.config.internal.serializer;

import com.oxywire.oxytowns.utils.IntRange;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class IntRangeSerializer implements TypeSerializer<IntRange> {

    public static final IntRangeSerializer INSTANCE = new IntRangeSerializer();

    private IntRangeSerializer() {
    }

    @Override
    public IntRange deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final @Nullable String range = node.getString();
        if (range == null) {
            throw new SerializationException("Range must not be null; Must be in form: `min-max` or `min`.");
        }

        final String[] split = range.split("-");
        final int min = Integer.parseInt(split[0]);
        final int max = split.length == 2 ? Integer.parseInt(split[1]) : min;

        return new IntRange(min, max);
    }

    @Override
    public void serialize(final Type type, final @Nullable IntRange obj, final ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            throw new SerializationException("Range must not be null; Must be in form: `min-max` or `min`.");
        }

        if (obj.min() == obj.max()) {
            node.set(obj.min());
        } else {
            node.set(obj.min() + "-" + obj.max());
        }
    }
}
