package com.oxywire.oxytowns.menu.town;

import com.oxywire.oxytowns.utils.Json;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.StorageGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.EnumSet;

public final class VaultMenu extends StorageGui {

    public VaultMenu(final int rows, final String title) {
        super(rows, title, EnumSet.noneOf(InteractionModifier.class));
    }

    public static ItemStack[] deserializeItemStacks(final String encoded) {
        final ItemStack[] items = new ItemStack[54];
        Arrays.fill(items, new ItemStack(Material.AIR));

        final byte[][] serialized = Json.GSON.fromJson(encoded, byte[][].class);

        for (int i = 0; i < serialized.length; i++) {
            final byte[] item = serialized[i];
            if (item == null || item.length == 0) continue;
            items[i] = ItemStack.deserializeBytes(item);
        }

        return items;
    }

    public static String serializeItemStacks(final ItemStack[] contents) {
        return Json.GSON.toJson(
            Arrays.stream(contents)
                .map(it -> {
                    if (it == null || it.getType() == Material.AIR) return new byte[0];
                    return it.serializeAsBytes();
                })
                .toArray(byte[][]::new)
        );
    }
}

