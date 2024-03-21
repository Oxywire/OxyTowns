package com.oxywire.oxytowns.menu.town;

import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import fr.minuskube.inv.content.InventoryContents;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;

import java.util.Map;

public final class TownMainMenu extends Menu {

    private final Town town;
    private final TagResolver[] placeholders;

    public TownMainMenu(final Town town) {
        this.town = town;
        this.placeholders = town.getPlaceholders();
    }

    public static void open(final Player player, final Town town) {
        Menu.builder(new TownMainMenu(town), town.getPlaceholders()).build().open(player);
    }

    @Override
    public void create(final Player player, final InventoryContents contents) {
        final Map<String, MenuElement> elements = getConfig().getElements();

        Menu.set(contents, elements.get("residents"), e -> MembersMenu.open(player, this.town), this.placeholders);
        Menu.set(contents, elements.get("bank"), this.placeholders);
        Menu.set(contents, elements.get("info"), this.placeholders);
        Menu.set(contents, elements.get("outposts"), e -> OutpostsMenu.open(player, this.town), this.placeholders);

        Menu.set(
            contents,
            elements.get("vault"),
            e -> {
                if (this.town.hasPermission(player.getUniqueId(), Permission.VAULT)) {
                    VaultSelectorMenu.open(player, this.town);
                } else {
                    Messages.get().getTown().getVault().getNoOpenPermission().send(player);
                }
            },
            placeholders
        );
        Menu.set(
            contents,
            elements.get("upgrades"),
            e -> {
                if (this.town.hasPermission(player.getUniqueId(), Permission.UPGRADES)) {
                    UpgradeMainMenu.open(player, this.town);
                } else {
                    Messages.get().getTown().getUpgrade().getNoPermission().send(player);
                }
            },
            this.placeholders
        );
        Menu.set(
            contents,
            elements.get("settings"),
            e -> {
                if (this.town.hasPermission(player.getUniqueId(), Permission.UPGRADES)) {
                    TownSettingsMenu.open(player, this.town);
                } else {
                    Messages.get().getTown().getSettingsNoPermission().send(player);
                }
            },
            this.placeholders
        );
    }
}
