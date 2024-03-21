package com.oxywire.oxytowns.entities.types;

import com.oxywire.oxytowns.config.Config;
import com.oxywire.oxytowns.config.messaging.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum Upgrade {
    CLAIMS(8),
    MEMBERS(3),
    VAULT_AMOUNT(0),
    OUTPOSTS(0);

    private final int defaultValue;

    public Map<Integer, Double> getTiers() {
        return Config.get().getUpgrades().get(this);
    }

    public TagResolver[] getPlaceholders(int tier) {
        Map<Integer, Double> tiers = getTiers();
        return new TagResolver[]{
            Placeholder.unparsed("upgrade", Message.formatEnum(this)),
            Formatter.number("amount", tiers.keySet().toArray(Integer[]::new)[tier]),
            Formatter.number("tier", tier + 1),
            Formatter.number("price", tiers.values().toArray(Double[]::new)[tier]),
        };
    }
}
