package com.oxywire.oxytowns.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LegacyPlotTypeMigrator {

    public String migrate(final String content) {
        return content
            .replace("\"MOB_FARM\"", "\"PRIVATE_MOB_FARM\"")
            .replace("plot-type-MOB_FARM:", "plot-type-PRIVATE_MOB_FARM:")
            .replace("\n  MOB_FARM:", "\n  PRIVATE_MOB_FARM:");
    }
}
