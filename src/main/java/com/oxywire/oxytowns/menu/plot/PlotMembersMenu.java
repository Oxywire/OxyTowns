package com.oxywire.oxytowns.menu.plot;

import com.oxywire.oxytowns.entities.impl.plot.Plot;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import com.oxywire.oxytowns.menu.PagedMenu;
import com.oxywire.oxytowns.utils.OfflinePlayerIsOnlineComparator;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
public final class PlotMembersMenu extends PagedMenu {

    private final Town town;
    private final Plot plot;

    public static void open(final Player player, final Town town, final Plot plot) {
        Menu.builder(new PlotMembersMenu(town, plot)).build().open(player);
    }

    @Override
    public ClickableItem[] createPaged(final Player player, final InventoryContents contents) {
        final Map<String, MenuElement> elements = getConfig().getElements();

        Menu.set(contents, elements.get("go-home"), e -> PlotMenu.open(player, this.town, this.plot));

        return this.plot.getAssignedMembers().stream()
            .map(Bukkit::getOfflinePlayer)
            .sorted(OfflinePlayerIsOnlineComparator.INSTANCE)
            .map(offlinePlayer -> {
                final ItemStack item = elements.get("member").getItem(
                    Placeholder.unparsed("name", Objects.requireNonNullElse(offlinePlayer.getName(), "null")),
                    Formatter.booleanChoice("status", offlinePlayer.isOnline())
                );
                item.editMeta(SkullMeta.class, meta -> meta.setOwningPlayer(offlinePlayer));

                return ClickableItem.empty(item);
            })
            .toArray(ClickableItem[]::new);
    }
}
