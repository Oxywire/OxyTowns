package com.oxywire.oxytowns.entities.impl;

import com.oxywire.oxytowns.utils.Placeholdered;
import lombok.Data;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;

import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Data
public final class BanEntry implements Placeholdered {

    private final UUID executor;
    private final UUID user;
    private final Date date;
    private final String reason;

    @Override
    public TagResolver[] getPlaceholders() {
        return new TagResolver[] {
            Formatter.date("date", this.date.toInstant().atZone(ZoneId.systemDefault())),
            Placeholder.unparsed("executor", Objects.requireNonNullElse(Bukkit.getOfflinePlayer(this.executor).getName(), "null")),
            Placeholder.unparsed("reason", this.reason),
            Placeholder.unparsed("name", Objects.requireNonNullElse(Bukkit.getOfflinePlayer(this.user).getName(), "null"))
        };
    }
}
