package com.oxywire.oxytowns.menu.town;

import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.Upgrade;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import fr.minuskube.inv.content.InventoryContents;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Map;

@AllArgsConstructor
public final class UpgradeMainMenu extends Menu {

    private final Town town;

    public static void open(final Player player, final Town town) {
        Menu.builder(new UpgradeMainMenu(town)).build().open(player);
    }

    @Override
    protected void create(final Player player, final InventoryContents contents) {
        final Map<String, MenuElement> elements = getConfig().getElements();

        Menu.set(contents, elements.get("go-home"), e -> TownMainMenu.open(player, this.town));
        Menu.set(contents, elements.get("info"));
        for (final Upgrade value : Upgrade.values()) {
            Menu.set(contents, elements.get("upgrade-" + value.name()), e -> UpgradeMenu.open(player, this.town, value));
        }
    }
}
