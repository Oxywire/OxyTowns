package com.oxywire.oxytowns.config.internal.serializer;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.spongepowered.configurate.serialize.ScalarSerializer;

public final class DurationSerializer extends ScalarSerializer<Duration> {

    public static final DurationSerializer INSTANCE = new DurationSerializer();
    private static final Pattern DURATION_PATTERN = Pattern.compile("(([1-9][0-9]+|[1-9])[dhms])");

    private DurationSerializer() {
        super(Duration.class);
    }

    @Override
    public Duration deserialize(final Type type, final Object obj) {
        String value = obj.toString().toLowerCase();
        Matcher matcher = DURATION_PATTERN.matcher(value);
        Duration duration = Duration.ofNanos(0);

        while (matcher.find()) {
            String group = matcher.group();
            String timeUnit = String.valueOf(group.charAt(group.length() - 1));
            int timeValue = Integer.parseInt(group.substring(0, group.length() - 1));
            switch (timeUnit) {
                case "d" -> duration = duration.plusDays(timeValue);
                case "h" -> duration = duration.plusHours(timeValue);
                case "m" -> duration = duration.plusMinutes(timeValue);
                case "s" -> duration = duration.plusSeconds(timeValue);
                default -> throw new IllegalArgumentException("Invalid duration character. Use format 1d2h3m4s");
            }
        }

        return duration;
    }


    @Override
    protected Object serialize(final Duration item, final Predicate<Class<?>> typeSupported) {
        return item.toString().substring(2);
    }
}
