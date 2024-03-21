package com.oxywire.oxytowns.config.messaging;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.oxywire.oxytowns.OxyTownsPlugin;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@Getter
@ConfigSerializable
public class Message {

    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    public static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();

    @Setting
    private String message = null;
    @Setting
    private String actionBar;
    @Setting
    private Title title;
    @Setting
    private Sound sound;
    @Setting
    private Particle particle;

    public CompletableFuture<Void> send(final Audience audience, final TagResolver... placeholders) {
        return this.send(audience, Map.of(), placeholders);
    }

    public CompletableFuture<Void> send(final Audience audience, final Map<String, String> replacements, final TagResolver... placeholders) {
        return CompletableFuture.runAsync(() -> {
            if (this.message != null) {
                // TODO: Make this nicer (fucking awful)
                final String[] msg = {this.message};
                replacements.forEach((key, value) -> msg[0] = msg[0].replace(key, value));
                audience.sendMessage(MINI_MESSAGE.deserialize(msg[0], placeholders));
            }
            if (this.actionBar != null) {
                audience.sendActionBar(MINI_MESSAGE.deserialize(this.actionBar, placeholders));
            }
            if (this.title != null && !this.title.isEmpty()) {
                audience.showTitle(this.title.asTitle(placeholders));
            }
            if (this.sound != null && !this.sound.isEmpty()) {
                // We either need to provide the sound's seed or play it sync to accommodate the rng
                Bukkit.getScheduler().runTask(OxyTownsPlugin.get(), () -> audience.playSound(sound.asSound()));
            }

            if (this.particle != null && !this.sound.isEmpty() && audience instanceof Player player) {
                player.spawnParticle(
                    this.particle.getParticle() == null ? org.bukkit.Particle.SPIT : this.particle.getParticle(),
                    player.getLocation(),
                    this.particle.getAmount(),
                    this.particle.getOffsetX(),
                    this.particle.getOffsetY(),
                    this.particle.getOffsetZ()
                );
            }
        });
    }

    public Message setMessage(final String message) {
        this.message = message;
        return this;
    }

    public Message setActionBar(final String actionBar) {
        this.actionBar = actionBar;
        return this;
    }

    public Message setTitle(final Title title) {
        this.title = title;
        return this;
    }

    public Message setSound(final Sound sound) {
        this.sound = sound;
        return this;
    }

    public Message setParticle(final Particle particle) {
        this.particle = particle;
        return this;
    }

    @ConfigSerializable
    public static class Title {

        @Setting
        private String title;
        @Setting
        private String subTitle;
        @Setting
        private Duration fadeIn;
        @Setting
        private Duration stay;
        @Setting
        private Duration fadeOut;

        public net.kyori.adventure.title.Title asTitle(final TagResolver... placeholders) {
            Component title = this.title == null ? Component.empty() : MINI_MESSAGE.deserialize(this.title, placeholders);
            Component subTitle = this.subTitle == null ? Component.empty() : MINI_MESSAGE.deserialize(this.subTitle, placeholders);

            if (this.fadeIn == null || this.stay == null || this.fadeOut == null) {
                return net.kyori.adventure.title.Title.title(title, subTitle);
            } else {
                return net.kyori.adventure.title.Title.title(title, subTitle, net.kyori.adventure.title.Title.Times.times(this.fadeIn, this.stay, this.fadeOut));
            }
        }

        public Title setTitle(final String title) {
            this.title = title;
            return this;
        }

        public Title setSubTitle(final String subTitle) {
            this.subTitle = subTitle;
            return this;
        }

        public Title setFadeIn(final Duration fadeIn) {
            this.fadeIn = fadeIn;
            return this;
        }

        public Title setStay(final Duration stay) {
            this.stay = stay;
            return this;
        }

        public Title setFadeOut(final Duration fadeOut) {
            this.fadeOut = fadeOut;
            return this;
        }

        public boolean isEmpty() {
            return this.title == null && this.subTitle == null;
        }
    }

    @ConfigSerializable
    public static class Sound {

        @Setting
        private String key;
        @Setting
        private net.kyori.adventure.sound.Sound.Source source;
        @Setting
        private Float volume;
        @Setting
        private Float pitch;

        public net.kyori.adventure.sound.Sound asSound() {
            return net.kyori.adventure.sound.Sound.sound(
                Key.key(this.key == null ? "block.bell.use" : this.key),
                this.source == null ? net.kyori.adventure.sound.Sound.Source.MASTER : this.source,
                this.volume == null ? 1 : this.volume,
                this.pitch == null ? 1 : this.pitch
            );
        }

        public Sound setKey(final String key) {
            this.key = key;
            return this;
        }

        public Sound setSource(final net.kyori.adventure.sound.Sound.Source source) {
            this.source = source;
            return this;
        }

        public Sound setVolume(final float volume) {
            this.volume = volume;
            return this;
        }

        public Sound setPitch(final float pitch) {
            this.pitch = pitch;
            return this;
        }

        public boolean isEmpty() {
            return this.key == null;
        }
    }

    @Getter
    @ConfigSerializable
    public static class Particle {

        @Setting
        private org.bukkit.Particle particle;
        @Setting
        private Integer amount;
        @Setting
        private Double offsetX;
        @Setting
        private Double offsetY;
        @Setting
        private Double offsetZ;

        public int getAmount() {
            return this.amount == null ? 1 : this.amount;
        }

        public double getOffsetX() {
            return this.offsetX == null ? 0 : this.offsetX;
        }

        public double getOffsetY() {
            return this.offsetY == null ? 0 : this.offsetY;
        }

        public double getOffsetZ() {
            return this.offsetZ == null ? 0 : this.offsetZ;
        }

        public boolean isEmpty() {
            return this.particle == null;
        }
    }

    public static String formatEnum(final Enum<?> receiver) {
        final String[] split = receiver.name().toLowerCase().split("_");
        final StringBuilder builder = new StringBuilder();
        for (final String s : split) {
            builder.append(s.substring(0, 1).toUpperCase()).append(s.substring(1)).append(" ");
        }
        return builder.toString().trim();
    }

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.getDefault());
    private static final NumberFormat FORMAT = NumberFormat.getNumberInstance(Locale.getDefault());

    public static String formatCurrency(Number number) {
        return CURRENCY_FORMAT.format(number);
    }

    public static String formatNumber(Number number) {
        return FORMAT.format(number);
    }
}

