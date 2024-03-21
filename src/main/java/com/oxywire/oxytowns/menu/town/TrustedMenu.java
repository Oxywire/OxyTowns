package com.oxywire.oxytowns.menu.town;

import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import com.oxywire.oxytowns.menu.PagedMenu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Map;

public final class TrustedMenu extends PagedMenu {

    private final Town town;

    public TrustedMenu(final Town town) {
        this.town = town;
    }

    public static void open(final Player player, final Town town) {
        Menu.builder(new TrustedMenu(town)).build().open(player);
    }

    @Override
    public ClickableItem[] createPaged(final Player player, final InventoryContents contents) {
        final Map<String, MenuElement> elements = getConfig().getElements();
        Menu.set(contents, elements.get("go-home"), e -> TownMainMenu.open(player, this.town));
        Menu.set(contents, elements.get("members"), e -> MembersMenu.open(player, this.town));
        Menu.set(contents, elements.get("ban"), e -> BansMenu.open(player, this.town));

        return this.town.getTrusted().stream()
            .map(it -> {
                final ItemStack item = elements.get("entry").getItem(it.getPlaceholders());
                item.editMeta(SkullMeta.class, (meta) -> meta.setOwningPlayer(Bukkit.getOfflinePlayer(it.getUser())));

                return ClickableItem.empty(item);
            }).toArray(ClickableItem[]::new);
    }
}
