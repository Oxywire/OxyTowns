package com.oxywire.oxytowns.menu.plot;

import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.config.messaging.Message;
import com.oxywire.oxytowns.entities.impl.plot.Plot;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.PlotType;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import fr.minuskube.inv.content.InventoryContents;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.Map;

@AllArgsConstructor
public final class PlotTypeMenu extends Menu {

    private final Town town;
    private final Plot plot;

    public static void open(final Player player, final Town town, final Plot plot) {
        Menu.builder(new PlotTypeMenu(town, plot)).build().open(player);
    }

    @Override
    protected void create(final Player player, final InventoryContents contents) {
        final Map<String, MenuElement> elements = getConfig().getElements();

        Menu.set(contents, elements.get("go-home"), e -> PlotMenu.open(player, this.town, this.plot));

        for (final PlotType plotType : PlotType.values()) {
            Menu.set(
                contents,
                elements.get("plot-type-" + plotType),
                e -> {
                    this.plot.setType(plotType);
                    Messages.get()
                        .getTown()
                        .getPlot()
                        .getType()
                        .getTypeChanged()
                        .send(player, Placeholder.unparsed("type", Message.formatEnum(plotType)));
                }
            );
        }
    }
}
