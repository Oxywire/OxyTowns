package com.oxywire.oxytowns.menu.town;

import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.commands.town.sub.SpawnCommand;
import com.oxywire.oxytowns.config.messaging.Message;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.settings.Setting;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import com.oxywire.oxytowns.menu.PagedMenu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.Map;

public final class TownsMenu extends PagedMenu {

    private final TownCache townCache;
    private final Comparator comparator;

    public TownsMenu(final TownCache townCache) {
        this.townCache = townCache;
        this.comparator = Comparator.NAME;
    }

    public TownsMenu(final TownCache townCache, final Comparator comparator) {
        this.townCache = townCache;
        this.comparator = comparator;
    }

    public static void open(final Player player, final TownCache townCache) {
        Menu.builder(new TownsMenu(townCache)).build().open(player);
    }

    public static void open(final Player player, final TownCache townCache, final Comparator comparator) {
        Menu.builder(new TownsMenu(townCache, comparator)).build().open(player);
    }

    @Override
    public ClickableItem[] createPaged(final Player player, final InventoryContents contents) {
        final Map<String, MenuElement> elements = getConfig().getElements();
        Menu.set(contents, elements.get("go-home"), e -> player.closeInventory());
        Menu.set(contents, elements.get("comparator"), e -> open(player, townCache, comparator.next()), Placeholder.unparsed("comparator", Message.formatEnum(comparator)));

        return this.townCache.getTowns().stream()
            .sorted(this.comparator.comparator.reversed())
            .map(town -> elements.get("list").getElement(e -> SpawnCommand.teleport(player, town), town.getPlaceholders()))
            .toArray(ClickableItem[]::new);
    }

    private enum Comparator {
        NAME((t1, t2) -> t2.getName().compareToIgnoreCase(t1.getName())),
        RESIDENTS(java.util.Comparator.comparingInt(t -> t.getMembers().size())),
        BALANCE(java.util.Comparator.comparingDouble(Town::getBankValue)),
        CLAIMS(java.util.Comparator.comparingInt(t -> t.getClaimedChunks().size())),
        OPEN(java.util.Comparator.comparing(t -> t.getToggle(Setting.OPEN))),
        AGE((t1, t2) -> t2.getCreationDate().compareTo(t1.getCreationDate()));

        private final java.util.Comparator<Town> comparator;

        Comparator(final java.util.Comparator<Town> comparator) {
            this.comparator = comparator;
        }

        public Comparator next() {
            final int index = ordinal();
            int nextIndex = index + 1;
            final Comparator[] comparators = Comparator.values();
            nextIndex %= comparators.length;
            return comparators[nextIndex];
        }
    }
}
