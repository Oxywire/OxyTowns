package com.oxywire.oxytowns.config;

import com.oxywire.oxytowns.OxyTownsPlugin;
import lombok.Getter;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@Getter
@Setter
public final class UpkeepTimes {

    private long lastUpkeep = 0;
    private long lastUpkeepWarning = 0;

    public static UpkeepTimes get() {
        return OxyTownsPlugin.configManager.get(UpkeepTimes.class);
    }
}
