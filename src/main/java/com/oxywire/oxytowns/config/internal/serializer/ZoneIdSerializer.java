package com.oxywire.oxytowns.config.internal.serializer;

import org.spongepowered.configurate.serialize.ScalarSerializer;

import java.lang.reflect.Type;
import java.time.ZoneId;
import java.util.function.Predicate;

public final class ZoneIdSerializer extends ScalarSerializer<ZoneId> {

    public static final ZoneIdSerializer INSTANCE = new ZoneIdSerializer();

    private ZoneIdSerializer() {
        super(ZoneId.class);
    }

    @Override
    public ZoneId deserialize(final Type type, final Object obj) {
        return ZoneId.of(obj.toString());
    }

    @Override
    protected Object serialize(final ZoneId item, final Predicate<Class<?>> typeSupported) {
        return item.getId();
    }
}
