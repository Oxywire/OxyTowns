package com.oxywire.oxytowns.menu.town;

import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import fr.minuskube.inv.content.InventoryContents;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import org.bukkit.entity.Player;

import java.util.Map;

@AllArgsConstructor
public final class VaultSelectorMenu extends Menu {

    private final Town town;

    public static void open(final Player player, final Town town) {
        Menu.builder(new VaultSelectorMenu(town)).build().open(player);
    }

    @Override
    protected void create(final Player player, final InventoryContents contents) {
        final Map<String, MenuElement> elements = getConfig().getElements();

        for (int i = 0; i < this.town.getVaults().size(); i++) {
            int finalI = i; // bruh
            contents.set(
                0,
                i,
                elements.get("vault").getElement(
                    e -> this.town.getVaults().get(finalI).open(player),
                    Formatter.number("number", i + 1)
                )
            );
        }
    }
}
