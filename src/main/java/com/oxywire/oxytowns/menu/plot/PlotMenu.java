package com.oxywire.oxytowns.menu.plot;

import com.oxywire.oxytowns.entities.impl.plot.Plot;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import fr.minuskube.inv.content.InventoryContents;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Map;

@AllArgsConstructor
public final class PlotMenu extends Menu {

    private final Town town;
    private final Plot plot;

    public static void open(final Player player, final Town town, final Plot plot) {
        Menu.builder(new PlotMenu(town, plot), plot.getPlaceholders()).build().open(player);
    }

    @Override
    protected void create(final Player player, final InventoryContents contents) {
        final Map<String, MenuElement> elements = getConfig().getElements();

        Menu.set(contents, elements.get("type"), e -> PlotTypeMenu.open(player, this.town, this.plot));
        Menu.set(contents, elements.get("members"), e -> PlotMembersMenu.open(player, this.town, this.plot));
    }
}
