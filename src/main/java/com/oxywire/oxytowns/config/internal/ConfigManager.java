package com.oxywire.oxytowns.config.internal;

import com.oxywire.oxytowns.config.internal.serializer.DurationSerializer;
import com.oxywire.oxytowns.config.internal.serializer.IntRangeSerializer;
import com.oxywire.oxytowns.config.internal.serializer.MessageSerializer;
import com.oxywire.oxytowns.config.internal.serializer.ZoneIdSerializer;
import com.oxywire.oxytowns.config.messaging.Message;
import com.oxywire.oxytowns.utils.IntRange;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ConfigManager {

    private final Map<Class<?>, Object> configs = new ConcurrentHashMap<>();
    private final Path dataDirectory;

    public ConfigManager(final Path dataDirectory) throws IOException {
        this.dataDirectory = dataDirectory;

        if (!Files.exists(this.dataDirectory)) {
            Files.createDirectories(this.dataDirectory);
        }
    }

    public <T> T get(final Class<T> clazz) {
        final Object config = this.configs.get(clazz);
        if (config != null) {
            return clazz.cast(config);
        }

        final T newConfig = this.create(clazz);
        this.configs.put(clazz, newConfig);
        return newConfig;
    }

    public void reload() {
        this.configs.replaceAll((clazz, config) -> this.create(clazz));
    }

    public void save(final Object config) {
        final Class<?> clazz = config.getClass();
        final Object conf = this.get(clazz);

        final YamlConfigurationLoader loader = this.loader(this.pathFor(clazz));
        try {
            final CommentedConfigurationNode node = loader.load();
            node.set(config.getClass(), conf);
            loader.save(node);
        } catch (final ConfigurateException e) {
            throw new RuntimeException(e);
        }

        this.configs.put(clazz, this.create(clazz));
    }

    private Path pathFor(final Class<?> clazz) {
        return this.dataDirectory.resolve(String.format("%s.yml", clazz.getSimpleName().toLowerCase()));
    }

    private YamlConfigurationLoader loader(final Path path) {
        return YamlConfigurationLoader.builder()
            .path(path)
            .nodeStyle(NodeStyle.BLOCK)
            .indent(2)
            .defaultOptions(options -> options
                .shouldCopyDefaults(true)
                .serializers(s -> s
                    .register(DurationSerializer.INSTANCE)
                    .register(ZoneIdSerializer.INSTANCE)
                    .register(Message.class, MessageSerializer.INSTANCE)
                    .register(IntRange.class, IntRangeSerializer.INSTANCE)
                )
            )
            .build();
    }

    private <T> T create(final Class<T> clazz) {
        try {
            final String name = String.format("%s.yml", clazz.getSimpleName().toLowerCase());
            final Path path = this.pathFor(clazz);

            if (!Files.exists(path)) {
                Files.createFile(path);
                try (final InputStream is = this.getClass().getResourceAsStream("/" + name)) {
                    if (is != null) Files.write(path, is.readAllBytes());
                }
            }

            final YamlConfigurationLoader loader = this.loader(path);
            final CommentedConfigurationNode node = loader.load();

            return node.get(clazz);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
