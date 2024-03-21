package com.oxywire.oxytowns.menu.town;

import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import com.oxywire.oxytowns.menu.PagedMenu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Map;

@AllArgsConstructor
public final class BansMenu extends PagedMenu {

    private final Town town;

    public static void open(final Player player, final Town town) {
        Menu.builder(new BansMenu(town)).build().open(player);
    }

    @Override
    public ClickableItem[] createPaged(final Player player, final InventoryContents contents) {
        Map<String, MenuElement> elements = getConfig().getElements();
        Menu.set(contents, elements.get("go-home"), e -> TownMainMenu.open(player, this.town));
        Menu.set(contents, elements.get("members"), e -> MembersMenu.open(player, this.town));
        Menu.set(contents, elements.get("trusted"), e -> TrustedMenu.open(player, this.town));

        return this.town.getBans().stream()
            .map(banEntry -> {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(banEntry.getUser()); // TODO: 12/8/2022 throw these in the constructor?
                ItemStack item = elements.get("ban-entry").getItem(banEntry.getPlaceholders());

                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setOwningPlayer(offlinePlayer);
                item.setItemMeta(meta);
                return ClickableItem.empty(item);
            })
            .toArray(ClickableItem[]::new);
    }
}
