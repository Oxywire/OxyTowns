package com.oxywire.oxytowns.menu.town;

import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import com.oxywire.oxytowns.menu.PagedMenu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

@AllArgsConstructor
public final class ClaimsMenu extends PagedMenu {

    private final Town town;

    public static void open(final Player player, final Town town) {
        Menu.builder(new ClaimsMenu(town)).build().open(player);
    }

    @Override
    public ClickableItem[] createPaged(final Player player, final InventoryContents contents) {
        return this.town.getClaimedChunks().stream()
            .map(chunk -> {
                Map<String, MenuElement> elements = getConfig().getElements();
                return elements.get("claims").getElement(
                    e -> player.teleportAsync(
                        chunk.getBukkitChunk().getBlock(
                            8,
                            Math.max(0, Bukkit.getWorld(chunk.getWorld()).getHighestBlockYAt(chunk.getX(), chunk.getZ())),
                            8
                        ).getLocation()
                    ),
                    Formatter.number("x", chunk.getX()),
                    Formatter.number("z", chunk.getZ()),
                    Placeholder.unparsed("world", chunk.getWorld())
                );
            })
            .toArray(ClickableItem[]::new);
    }
}
