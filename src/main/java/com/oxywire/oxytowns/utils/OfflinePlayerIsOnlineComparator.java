package com.oxywire.oxytowns.utils;

import org.bukkit.OfflinePlayer;

import java.util.Comparator;
import java.util.Objects;

public final class OfflinePlayerIsOnlineComparator implements Comparator<OfflinePlayer> {

    public static final OfflinePlayerIsOnlineComparator INSTANCE = new OfflinePlayerIsOnlineComparator();

    @Override
    public int compare(final OfflinePlayer o1, final OfflinePlayer o2) {
        final boolean aOnline = o1.isOnline();
        final boolean bOnline = o2.isOnline();

        if (aOnline && !bOnline) {
            return -1;
        } else if (!aOnline && bOnline) {
            return 1;
        } else {
            return Objects.requireNonNullElse(o1.getName(), "null1").compareTo(Objects.requireNonNullElse(o2.getName(), "null2"));
        }
    }
}
