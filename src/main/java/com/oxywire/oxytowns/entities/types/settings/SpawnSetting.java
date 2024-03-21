package com.oxywire.oxytowns.entities.types.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpawnSetting {
    EVERYONE("Everyone"),
    TRUSTED("Trusted"),
    MEMBERS("Members");

    private final String name;

    /**
     * Helper method to toggle through settings.
     *
     * @param setting the setting to get from the enum
     * @return enum setting value
     */
    public SpawnSetting getNextSetting(final SpawnSetting setting) {
        final int index = setting.ordinal();
        int nextIndex = index + 1;
        final SpawnSetting[] spawnSettings = SpawnSetting.values();
        nextIndex %= spawnSettings.length;
        return spawnSettings[nextIndex];
    }
}
